package cn.net.ziqiang.teamup.backend.service.business.impl

import cn.net.ziqiang.teamup.backend.common.annotation.Slf4j
import cn.net.ziqiang.teamup.backend.common.annotation.Slf4j.Companion.logger
import cn.net.ziqiang.teamup.backend.common.constant.type.ResultType
import cn.net.ziqiang.teamup.backend.common.exception.ApiException
import cn.net.ziqiang.teamup.backend.service.annotation.Business
import cn.net.ziqiang.teamup.backend.service.business.OssBusiness
import cn.net.ziqiang.teamup.backend.service.cache.OssCacheManager
import cn.net.ziqiang.teamup.backend.service.properties.OssProperties
import cn.net.ziqiang.teamup.backend.service.util.FileUtil
import cn.net.ziqiang.teamup.backend.service.vo.file.OssTokenVO
import com.alibaba.fastjson.JSONObject
import com.aliyun.oss.OSS
import com.aliyun.oss.OSSClientBuilder
import com.aliyun.oss.common.utils.BinaryUtil
import com.aliyun.oss.model.MatchMode
import com.aliyun.oss.model.PolicyConditions
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.HttpClients
import org.apache.http.util.EntityUtils
import org.bouncycastle.util.io.pem.PemReader
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import org.springframework.web.multipart.MultipartFile
import java.io.IOException
import java.io.InputStream
import java.io.StringReader
import java.net.URLDecoder
import java.nio.file.Path
import java.security.KeyFactory
import java.security.Signature
import java.security.spec.X509EncodedKeySpec
import java.util.*


@Slf4j
@Business
class OssBusinessImpl : OssBusiness {
    @Autowired
    private lateinit var ossProperties: OssProperties
    @Autowired
    private lateinit var ossCacheManager: OssCacheManager

    private var ossClient: OSS? = null
        get() {
            if (field == null) {
                synchronized(this) {
                    if (field == null) {
                        field = OSSClientBuilder().build(ossProperties.endpoint, ossProperties.accessKeyId, ossProperties.accessKeySecret)
                    }
                }
            }
            return field
        }

    override fun uploadFile(path: Path, fileName: String, stream: InputStream): String {
        try {
            val objectKey = path.resolve(fileName).toString().replace("\\", "/")

            ossClient!!.putObject(ossProperties.bucketName, objectKey, stream)

            return objectKey
        } catch (e: Exception) {
            logger.error(e.stackTraceToString())
        }
        throw ApiException(ResultType.ThirdServiceError, "OSS上传失败")
    }

    override fun deleteFile(objectKey: String) {
        try {
            ossClient!!.deleteObject(ossProperties.bucketName, objectKey)
            return
        } catch (e: Exception) {
            logger.error(e.stackTraceToString())
        }
        throw ApiException(ResultType.ThirdServiceError, "OSS文件删除失败")
    }

    override fun deleteFileByUrl(url: String) {
        deleteFile(getObjectKey(url))
    }

    override fun uploadAvatar(file: MultipartFile): String {
        val fileName = FileUtil.getRandomFileName(file.originalFilename!!)
        val path = Path.of("media", "avatar")
        val stream = file.inputStream
        return getUrl(uploadFile(path, fileName, stream))
    }

    override fun generateToken(path: Path, fileName: String, callbackPath: String): OssTokenVO {
        val expiration = Date(System.currentTimeMillis() + 30 * 1000)
        val objectKey = path.resolve(FileUtil.getRandomFileName(fileName)).toString().replace("\\", "/")
        val policy = PolicyConditions()
        policy.addConditionItem(PolicyConditions.COND_CONTENT_LENGTH_RANGE, 0, 100 * 1024 * 1024)
        policy.addConditionItem(MatchMode.Exact, PolicyConditions.COND_KEY, objectKey)

        val postPolicy = ossClient!!.generatePostPolicy(expiration, policy)
        val binaryData = postPolicy.toByteArray(charset("utf-8"))
        val encodedPolicy = BinaryUtil.toBase64String(binaryData)
        val postSignature = ossClient!!.calculatePostSignature(postPolicy)

        val jasonCallback = JSONObject().apply {
            put("callbackUrl", "https://api.teamup.nagico.cn$callbackPath")
            put("callbackBodyType", "application/json")
            put("callbackBody", JSONObject().apply {
                put("objectKey", "\${object}")
                put("size", "\${size}")
            }.toString())
        }
        val base64CallbackBody = BinaryUtil.toBase64String(jasonCallback.toString().toByteArray())

        return OssTokenVO(
            accessKeyId = ossProperties.accessKeyId,
            policy = encodedPolicy,
            signature = postSignature,
            host = "https://${ossProperties.bucketName}.${ossProperties.endpoint}",
            expire = expiration,
            callback = base64CallbackBody,
            key = objectKey
        )

    }

    override fun verifyCallback() {
        val request = (RequestContextHolder.currentRequestAttributes() as ServletRequestAttributes).request
        val requestBody = getPostBody(request.inputStream, request.getHeader("content-length").toInt())

        var ret = false
        val autorizationInput = request.getHeader("Authorization")
        val pubKeyInput = request.getHeader("x-oss-pub-key-url")
        val authorization = BinaryUtil.fromBase64String(autorizationInput)
        val pubKey = BinaryUtil.fromBase64String(pubKeyInput)
        val pubKeyAddr = String(pubKey)
        if (!pubKeyAddr.startsWith("http://gosspublic.alicdn.com/")
            && !pubKeyAddr.startsWith("https://gosspublic.alicdn.com/")
        ) {
            logger.error("pub key addr must be oss addrss")
            throw ApiException(ResultType.ThirdServiceError, "OSS回调验证失败")
        }
        val retString: String = executeGet(pubKeyAddr)
        val queryString: String? = request.queryString
        val uri: String = request.requestURI
        val decodeUri = URLDecoder.decode(uri, "UTF-8")
        var authStr = decodeUri
        if (!queryString.isNullOrEmpty()) {
            authStr += "?$queryString"
        }
        authStr += """
            
            $requestBody
            """.trimIndent()
        ret = doCheck(authStr, authorization, retString)
        if (!ret) {
            logger.error("oss callback failed")
            throw ApiException(ResultType.ThirdServiceError, "OSS回调验证失败")
        }
    }

    override fun getUrl(objectKey: String): String {
        val _prefix = "https://${ossProperties.bucketName}.${ossProperties.endpoint}/"
        return _prefix + objectKey
    }

    override fun getObjectKey(url: String): String {
        val _prefix = "https://${ossProperties.bucketName}.${ossProperties.endpoint}/"
        return url.replace(_prefix, "")
    }

    // region Utils
    private fun executeGet(url: String): String {
        return ossCacheManager.getKeyCache(url) ?: run {
            val httpClient = HttpClients.createDefault()
            val httpGet = HttpGet(url)
            val response = httpClient.execute(httpGet)
            val entity = response.entity
            val responseContent = EntityUtils.toString(entity, "UTF-8")
            ossCacheManager.setKeyCache(url, responseContent)
            responseContent
        }
    }

    private fun doCheck(authStr: String, signature: ByteArray, pubKey: String): Boolean {
        try {
            StringReader(pubKey).use { reader ->
                PemReader(reader).use { pemReader ->
                    val fact = KeyFactory.getInstance("RSA")
                    val pemObject = pemReader.readPemObject()
                    val keyContentAsBytesFromBC: ByteArray = pemObject.content
                    val pubKeySpec = X509EncodedKeySpec(keyContentAsBytesFromBC)
                    val publicKey = fact.generatePublic(pubKeySpec)

                    val signatureChecker = Signature.getInstance("MD5withRSA")
                    signatureChecker.initVerify(publicKey)
                    signatureChecker.update(authStr.toByteArray())
                    return signatureChecker.verify(signature)
                }
            }
        } catch (t: Throwable) {
            t.printStackTrace()
            return false
        }
    }

    private fun getPostBody(inputStream: InputStream, contentLen: Int): String {
        if (contentLen > 0) {
            var readLen = 0
            var readLengthThisTime = 0
            val message = ByteArray(contentLen)
            try {
                while (readLen != contentLen) {
                    readLengthThisTime = inputStream.read(message, readLen, contentLen - readLen)
                    if (readLengthThisTime == -1) { // Should not happen.
                        break
                    }
                    readLen += readLengthThisTime
                }
                return String(message)
            } catch (e: IOException) {
                logger.error(e.stackTraceToString())
            }
        }
        return ""
    }
    // endregion
}
