package cn.net.ziqiang.teamup.backend.web.controller

import cn.net.ziqiang.teamup.backend.common.pojo.vo.auth.AuthVO
import cn.net.ziqiang.teamup.backend.common.pojo.vo.user.RegisterDto
import cn.net.ziqiang.teamup.backend.common.pojo.vo.user.ResetPasswordDto
import cn.net.ziqiang.teamup.backend.common.pojo.vo.user.*
import cn.net.ziqiang.teamup.backend.service.service.UserService
import cn.net.ziqiang.teamup.backend.web.annotation.user.NormalUser
import cn.net.ziqiang.teamup.backend.web.security.SecurityContextUtils
import cn.net.ziqiang.teamup.backend.web.annotation.user.ActiveUser
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import javax.annotation.security.PermitAll
import javax.validation.Valid

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

    @PermitAll
    @Operation(summary = "注册")
    @PostMapping
    fun register(@Valid @RequestBody dto: RegisterDto): AuthVO {
        return AuthVO(userService.register(dto))
    }

    @PermitAll
    @Operation(summary = "重置密码")
    @PostMapping("/password/reset")
    fun resetPassword(@Valid @RequestBody dto: ResetPasswordDto): Map<String, String> {
        userService.resetPassword(dto)
        return mapOf("status" to "success")
    }

    @PermitAll
    @Operation(summary = "查询用户名或手机号数量")
    @GetMapping("/count/")
    fun countByUsername(
        @RequestParam(defaultValue = "") username: String,
        @RequestParam(defaultValue = "") phone: String
    ) : Map<String, Int> {
        val res = mutableMapOf<String, Int>()
        if (username.isNotBlank()) {
            res["username"] = userService.countByUsername(username)
        }
        if (phone.isNotBlank()) {
            res["phone"] = userService.countByPhone(phone)
        }
        return res
    }

    @NormalUser
    @Operation(summary = "修改密码")
    @PutMapping("/password")
    fun changePassword(@Valid @RequestBody dto: ChangePasswordDto) : Map<String, String> {
        userService.changePassword(SecurityContextUtils.userId, dto)
        return mapOf("status" to "success")
    }

    @NormalUser
    @Operation(summary = "修改手机号")
    @PutMapping("/phone")
    fun changePhone(@Valid @RequestBody dto: ChangePhoneDto) : Map<String, String> {
        userService.changePhone(SecurityContextUtils.userId, dto)
        return mapOf("status" to "success")
    }

    @NormalUser
    @Operation(summary = "更新个人资料")
    @PutMapping("")
    fun updateProfile(@Valid @RequestBody userProfile: UpdateUserProfileDto) : UserProfileVO {
        return userService.updateUser(SecurityContextUtils.userId, userProfile)
    }

    @NormalUser
    @Operation(summary = "上传头像")
    @PostMapping("/avatar")
    fun uploadAvatar(@RequestParam avatar: MultipartFile) : UserProfileVO {
        return userService.updateUserAvatar(SecurityContextUtils.userId, avatar)
    }
}