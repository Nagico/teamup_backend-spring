package cn.net.ziqiang.teamup.backend.web.controller

import cn.net.ziqiang.teamup.backend.common.pojo.dto.user.UpdateUserProfileDto
import cn.net.ziqiang.teamup.backend.service.service.user.UserService
import cn.net.ziqiang.teamup.backend.common.pojo.vo.user.UserInfo
import cn.net.ziqiang.teamup.backend.web.annotation.user.NormalUser
import cn.net.ziqiang.teamup.backend.web.security.SecurityContextUtils
import cn.net.ziqiang.teamup.backend.common.pojo.vo.user.UserProfileVO
import cn.net.ziqiang.teamup.backend.web.annotation.user.ActiveUser
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@Tag(name = "用户")
@RestController
@RequestMapping("/users")
class UserController {
    @Autowired
    private lateinit var userService: UserService

    @NormalUser
    @Operation(summary = "获取个人信息")
    @GetMapping("")
    fun getCurrentUser() : UserProfileVO {
        return userService.getUserProfileById(SecurityContextUtils.userId)
    }

    @ActiveUser
    @Operation(summary = "获取用户简要信息")
    @GetMapping("/{id}")
    fun getUserProfileById(@PathVariable id: Long) : UserInfo {
        val currentUserId = SecurityContextUtils.userId

        return if (currentUserId == id) {
            userService.getUserProfileById(id)
        } else {
            userService.getUserInfoById(id)
        }
    }

    @NormalUser
    @Operation(summary = "查询用户名数量")
    @GetMapping("/count/")
    fun countByUsername(@RequestParam username: String) : Map<String, Int> {
        return mapOf("count" to userService.countByUsername(username))
    }

    @NormalUser
    @Operation(summary = "更新个人资料")
    @PutMapping("")
    fun updateProfile(@RequestBody userProfile: UpdateUserProfileDto) : UserProfileVO {
        return userService.updateUser(SecurityContextUtils.userId, userProfile)
    }

    @NormalUser
    @Operation(summary = "上传头像")
    @PostMapping("/avatar")
    fun uploadAvatar(@RequestParam avatar: MultipartFile) : UserProfileVO {
        return userService.updateUserAvatar(SecurityContextUtils.userId, avatar)
    }
}