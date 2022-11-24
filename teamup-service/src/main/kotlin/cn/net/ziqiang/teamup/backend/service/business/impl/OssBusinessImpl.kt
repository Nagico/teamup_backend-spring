package cn.net.ziqiang.teamup.backend.service.business.impl

import cn.net.ziqiang.teamup.backend.common.annotation.Slf4j
import cn.net.ziqiang.teamup.backend.common.annotation.Slf4j.Companion.logger
import cn.net.ziqiang.teamup.backend.common.constant.type.ResultType
import cn.net.ziqiang.teamup.backend.common.exception.ApiException
import cn.net.ziqiang.teamup.backend.service.annotation.Business
import cn.net.ziqiang.teamup.backend.service.business.OssBusiness
import cn.net.ziqiang.teamup.backend.service.properties.OssProperties
import cn.net.ziqiang.teamup.backend.service.util.FileUtil
import com.aliyun.oss.OSS
import com.aliyun.oss.OSSClientBuilder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.multipart.MultipartFile
import java.io.InputStream
import java.nio.file.Path


@Slf4j
@Business
class OssBusinessImpl : OssBusiness {
    @Autowired
    private lateinit var ossProperties: OssProperties

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

    // region Utils
    private fun getUrl(objectKey: String): String {
        val _prefix = "https://${ossProperties.bucketName}.${ossProperties.endpoint}/"
        return _prefix + objectKey
    }

    private fun getObjectKey(url: String): String {
        val _prefix = "https://${ossProperties.bucketName}.${ossProperties.endpoint}/"
        return url.replace(_prefix, "")
    }
    // endregion
}
