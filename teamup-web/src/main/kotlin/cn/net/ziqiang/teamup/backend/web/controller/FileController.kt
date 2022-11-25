package cn.net.ziqiang.teamup.backend.web.controller

import cn.net.ziqiang.teamup.backend.common.constant.type.FileType
import cn.net.ziqiang.teamup.backend.common.entity.File
import cn.net.ziqiang.teamup.backend.service.service.file.FileService
import cn.net.ziqiang.teamup.backend.service.vo.file.OssCallbackVO
import cn.net.ziqiang.teamup.backend.service.vo.file.OssTokenVO
import cn.net.ziqiang.teamup.backend.web.annotation.permission.OwnerOrManager
import cn.net.ziqiang.teamup.backend.web.annotation.user.ActiveUser
import cn.net.ziqiang.teamup.backend.web.security.SecurityContextUtils
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@Tag(name = "文件")
@RestController
@RequestMapping("/files")
class FileController {
    @Autowired
    private lateinit var fileService: FileService

    @ActiveUser
    @Operation(summary = "查询文件")
    @OwnerOrManager("file")
    @GetMapping("/{id}")
    fun getFile(@PathVariable id: Long): File {
        return fileService.getFile(id)
    }

    @ActiveUser
    @Operation(summary = "撤回文件")
    @OwnerOrManager("file")
    @DeleteMapping("/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    fun deleteFile(@PathVariable id: Long) {
        return fileService.deleteFile(id)
    }

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
        @RequestBody ossCallbackVO: OssCallbackVO
    ) : File {
        return fileService.callback(id, ossCallbackVO)
    }
}