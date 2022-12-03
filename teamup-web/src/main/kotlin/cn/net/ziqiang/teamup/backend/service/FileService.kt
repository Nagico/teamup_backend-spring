package cn.net.ziqiang.teamup.backend.service

import cn.net.ziqiang.teamup.backend.constant.type.FileType
import cn.net.ziqiang.teamup.backend.pojo.entity.File
import cn.net.ziqiang.teamup.backend.pojo.entity.User
import cn.net.ziqiang.teamup.backend.pojo.vo.file.OssCallbackVO
import cn.net.ziqiang.teamup.backend.pojo.vo.file.OssTokenVO

interface FileService {
    /**
     * 获取文件
     *
     * @param id
     * @return
     */
    fun getFile(id: Long): File

    /**
     * 删除文件
     *
     * @param id
     */
    fun deleteFile(id: Long)

    /**
     * 生成直传Token
     *
     * @param user
     * @param fileName
     * @param type
     * @return
     */
    fun generateToken(user: User, fileName: String, type: FileType): OssTokenVO

    /**
     * 验证直传回调签名
     *
     * @param fileId
     * @param ossCallbackVO
     * @return
     */
    fun callback(fileId: Long, ossCallbackVO: OssCallbackVO): File

    fun fileExpireScheduler()
}