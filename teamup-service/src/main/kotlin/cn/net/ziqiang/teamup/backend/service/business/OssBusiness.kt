package cn.net.ziqiang.teamup.backend.service.business

import cn.net.ziqiang.teamup.backend.common.pojo.vo.file.OssTokenVO
import org.springframework.web.multipart.MultipartFile
import java.io.InputStream
import java.nio.file.Path

interface OssBusiness {
    /**
     * OSS上传文件
     *
     * @param path 上传路径
     * @param fileName 文件名
     * @param stream 文件流
     * @return objectKey
     */
    fun uploadFile(path: Path, fileName: String, stream: InputStream): String

    /**
     * 删除文件
     *
     * @param objectKey object key
     */
    fun deleteFile(objectKey: String)

    /**
     * TODO
     *
     * @param url 文件url
     */
    fun deleteFileByUrl(url: String)

    /**
     * 上传头像
     *
     * @param file 文件
     * @return url
     */
    fun uploadAvatar(file: MultipartFile): String

    /**
     * 生成直传Token
     *
     * @return
     */
    fun generateToken(path: Path, fileName: String, callbackPath: String): OssTokenVO

    /**
     * 验证直传回调签名
     *
     * @return
     */
    fun verifyCallback()

    /**
     * 获取文件url
     *
     * @param objectKey
     * @return
     */
    fun getUrl(objectKey: String): String

    /**
     * 获取文件object key
     *
     * @param url
     * @return
     */
    fun getObjectKey(url: String): String
}