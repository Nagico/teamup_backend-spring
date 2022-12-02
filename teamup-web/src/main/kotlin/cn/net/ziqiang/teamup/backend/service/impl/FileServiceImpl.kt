package cn.net.ziqiang.teamup.backend.service.impl

import cn.net.ziqiang.teamup.backend.constant.type.FileType
import cn.net.ziqiang.teamup.backend.constant.type.ResultType
import cn.net.ziqiang.teamup.backend.pojo.entity.File
import cn.net.ziqiang.teamup.backend.pojo.entity.User
import cn.net.ziqiang.teamup.backend.pojo.exception.ApiException
import cn.net.ziqiang.teamup.backend.dao.repository.FileRepository
import cn.net.ziqiang.teamup.backend.business.OssBusiness
import cn.net.ziqiang.teamup.backend.pojo.vo.file.OssCallbackVO
import cn.net.ziqiang.teamup.backend.pojo.vo.file.OssTokenVO
import cn.net.ziqiang.teamup.backend.service.FileService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.nio.file.Path
import java.util.*

@Service
@EnableAsync
@EnableScheduling
class FileServiceImpl : cn.net.ziqiang.teamup.backend.service.FileService {
    @Autowired
    private lateinit var ossBusiness: OssBusiness
    @Autowired
    private lateinit var fileRepository: FileRepository
    override fun getFile(id: Long): cn.net.ziqiang.teamup.backend.pojo.entity.File {
        return fileRepository.findById(id).orElseThrow { ApiException(ResultType.ResourceNotFound) }.checkPermission()
    }

    override fun deleteFile(id: Long) {
        fileRepository.delete(getFile(id))
    }

     private fun expireFile(file: cn.net.ziqiang.teamup.backend.pojo.entity.File) {
        file.expired = true
        try {
            ossBusiness.deleteFileByUrl(file.url)
        } catch (_: Exception) { }

        fileRepository.save(file)
    }

    @Transactional
    override fun generateToken(user: User, fileName: String, type: FileType): OssTokenVO {
        if (fileName.isEmpty())
            throw ApiException(ResultType.ParamValidationFailed, "文件名不能为空")

        var file = cn.net.ziqiang.teamup.backend.pojo.entity.File(
            user = user,
            name = fileName,
            type = type,
            expired = false,
            createTime = Date()
        )
        file = fileRepository.save(file)

        return ossBusiness.generateToken(
            fileName = fileName,
            path = Path.of("media", type.string),
            callbackPath = "/files/${file.id}/callback/"
        )
    }

    @Transactional
    override fun callback(fileId: Long, ossCallbackVO: OssCallbackVO): cn.net.ziqiang.teamup.backend.pojo.entity.File {
        ossBusiness.verifyCallback()
        val file = getFile(fileId)

        file.url = ossBusiness.getUrl(ossCallbackVO.objectKey)
        file.size = ossCallbackVO.size
        file.createTime = Date()

        return fileRepository.save(file)
    }

    @Scheduled(cron = "0 15 2 * * ?")
    override fun fileExpireScheduler() {
        val files = fileRepository.findByExpiredFalseOrderByUser_CreateTimeAsc()
        files.forEach {
            if (it.createTime!!.time + 1000 * 60 * 60 * 24 * 7 < Date().time)
                expireFile(it)
        }
    }
}