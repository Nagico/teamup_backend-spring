package cn.net.ziqiang.teamup.backend.service.service.file

import cn.net.ziqiang.teamup.backend.common.constant.type.FileType
import cn.net.ziqiang.teamup.backend.common.constant.type.ResultType
import cn.net.ziqiang.teamup.backend.common.entity.File
import cn.net.ziqiang.teamup.backend.common.entity.User
import cn.net.ziqiang.teamup.backend.common.exception.ApiException
import cn.net.ziqiang.teamup.backend.dao.repository.FileRepository
import cn.net.ziqiang.teamup.backend.service.business.OssBusiness
import cn.net.ziqiang.teamup.backend.service.vo.file.OssCallbackVO
import cn.net.ziqiang.teamup.backend.service.vo.file.OssTokenVO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.nio.file.Path
import java.util.*

@Service
class FileServiceImpl : FileService {
    @Autowired
    private lateinit var ossBusiness: OssBusiness
    @Autowired
    private lateinit var fileRepository: FileRepository
    override fun getFile(id: Long): File {
        return fileRepository.findById(id).orElseThrow { ApiException(ResultType.ResourceNotFound) }
    }

    @Transactional
    override fun generateToken(user: User, fileName: String, type: FileType): OssTokenVO {
        if (fileName.isEmpty())
            throw ApiException(ResultType.ParamValidationFailed, "文件名不能为空")

        var file = File(
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
            callbackPath = "/file/${file.id}/callback/"
        )
    }

    @Transactional
    override fun callback(fileId: Long, ossCallbackVO: OssCallbackVO): File {
        ossBusiness.verifyCallback()
        val file = getFile(fileId)

        file.url = ossBusiness.getUrl(ossCallbackVO.objectKey)
        file.size = ossCallbackVO.size
        file.createTime = Date()

        return fileRepository.save(file)
    }
}