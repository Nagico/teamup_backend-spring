package cn.net.ziqiang.teamup.backend.service.service.file

import cn.net.ziqiang.teamup.backend.common.constant.type.FileType
import cn.net.ziqiang.teamup.backend.common.entity.File
import cn.net.ziqiang.teamup.backend.common.entity.User
import cn.net.ziqiang.teamup.backend.service.vo.file.CallbackVO
import cn.net.ziqiang.teamup.backend.service.vo.file.OssTokenVO

interface FileService {
    /**
     * 获取文件
     *
     * @param id
     * @return
     */
    fun getFile(id: Long): File

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
     * @param callbackVO
     * @return
     */
    fun callback(fileId: Long, callbackVO: CallbackVO): File
}