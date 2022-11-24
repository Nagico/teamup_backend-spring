package cn.net.ziqiang.teamup.backend.web.controller

import cn.net.ziqiang.teamup.backend.common.constant.type.FileType
import cn.net.ziqiang.teamup.backend.common.entity.File
import cn.net.ziqiang.teamup.backend.service.service.file.FileService
import cn.net.ziqiang.teamup.backend.service.vo.file.CallbackVO
import cn.net.ziqiang.teamup.backend.service.vo.file.OssTokenVO
import cn.net.ziqiang.teamup.backend.web.annotation.user.ActiveUser
import cn.net.ziqiang.teamup.backend.web.security.SecurityContextUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/files")
class FileController {
    @Autowired
    private lateinit var fileService: FileService

    @ActiveUser
    @GetMapping("/tokens")
    fun genToken(
        @RequestParam name: String,
        @RequestParam type: FileType
    ): OssTokenVO {
        val user = SecurityContextUtils.user

        return fileService.generateToken(user, name, type)
    }

    @PostMapping("/{id}/callback")
    fun ossCallback(
        @PathVariable id: Long,
        @RequestBody callbackVO: CallbackVO
    ) : File {
        return fileService.callback(id, callbackVO)
    }
}