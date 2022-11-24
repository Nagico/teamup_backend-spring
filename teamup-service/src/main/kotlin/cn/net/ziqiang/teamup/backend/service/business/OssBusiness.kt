package cn.net.ziqiang.teamup.backend.service.business

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
}