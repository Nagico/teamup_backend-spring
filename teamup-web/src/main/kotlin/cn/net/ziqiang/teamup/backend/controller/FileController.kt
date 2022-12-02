package cn.net.ziqiang.teamup.backend.controller

import cn.net.ziqiang.teamup.backend.constant.type.FileType
import cn.net.ziqiang.teamup.backend.pojo.entity.File
import cn.net.ziqiang.teamup.backend.service.FileService
import cn.net.ziqiang.teamup.backend.pojo.vo.file.OssCallbackVO
import cn.net.ziqiang.teamup.backend.pojo.vo.file.OssTokenVO
import cn.net.ziqiang.teamup.backend.util.annotation.permission.OwnerOrManager
import cn.net.ziqiang.teamup.backend.util.annotation.user.ActiveUser
import cn.net.ziqiang.teamup.backend.util.security.SecurityContextUtils
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
    private lateinit var fileService: cn.net.ziqiang.teamup.backend.service.FileService

    @ActiveUser
    @Operation(summary = "查询文件")
    @OwnerOrManager("file")
    @GetMapping("/{id}")
    fun getFile(@PathVariable id: Long): cn.net.ziqiang.teamup.backend.pojo.entity.File {
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
    ) : cn.net.ziqiang.teamup.backend.pojo.entity.File {
        return fileService.callback(id, ossCallbackVO)
    }
}