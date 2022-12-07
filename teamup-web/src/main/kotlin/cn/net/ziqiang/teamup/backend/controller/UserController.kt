package cn.net.ziqiang.teamup.backend.controller

import cn.net.ziqiang.teamup.backend.constant.type.ResultType
import cn.net.ziqiang.teamup.backend.pojo.exception.ApiException
import cn.net.ziqiang.teamup.backend.pojo.entity.User
import cn.net.ziqiang.teamup.backend.pojo.vo.auth.AuthVO
import cn.net.ziqiang.teamup.backend.pojo.vo.user.*
import cn.net.ziqiang.teamup.backend.service.UserService
import cn.net.ziqiang.teamup.backend.util.annotation.user.NormalUser
import cn.net.ziqiang.teamup.backend.util.security.SecurityContextUtils
import cn.net.ziqiang.teamup.backend.util.annotation.user.ActiveUser
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
    fun getCurrentUser() : User {
        return userService.getUserProfileById(SecurityContextUtils.userId)
    }

    @PermitAll
    @Operation(summary = "获取用户简要信息")
    @GetMapping("/{id}")
    fun getUserProfileById(@PathVariable id: Long) : User {
        return userService.getUserInfoById(id)
    }

    @PermitAll
    @Operation(summary = "注册")
    @PostMapping
    fun register(@Valid @RequestBody dto: UserDto): AuthVO {
        if (dto.phone.isEmpty() || dto.verifyCode.isEmpty() || dto.password.isEmpty()) {
            throw ApiException(ResultType.ParamEmpty)
        }

        if (!dto.phone.matches(Regex("^1[3-9]\\d{9}$"))) {
            throw ApiException(ResultType.ParamValidationFailed, "手机号码格式不正确")
        }

        return AuthVO(userService.register(dto))
    }

    @PermitAll
    @Operation(summary = "重置密码")
    @PostMapping("/password/reset")
    fun resetPassword(@Valid @RequestBody dto: UserDto): Map<String, String> {
        if (dto.phone.isEmpty() || dto.verifyCode.isEmpty() || dto.password.isEmpty()) {
            throw ApiException(ResultType.ParamEmpty)
        }

        if (!dto.phone.matches(Regex("^1[3-9]\\d{9}$"))) {
            throw ApiException(ResultType.ParamValidationFailed, "手机号码格式不正确")
        }

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
    fun changePassword(@Valid @RequestBody dto: UserDto) : Map<String, String> {
        if (dto.password.isEmpty() || dto.oldPassword.isEmpty()) {
            throw ApiException(ResultType.ParamEmpty)
        }

        userService.changePassword(SecurityContextUtils.userId, dto)
        return mapOf("status" to "success")
    }

    @NormalUser
    @Operation(summary = "修改手机号")
    @PutMapping("/phone")
    fun changePhone(@Valid @RequestBody dto: UserDto) : Map<String, String> {
        if (dto.phone.isEmpty() || dto.verifyCode.isEmpty()) {
            throw ApiException(ResultType.ParamEmpty)
        }

        if (!dto.phone.matches(Regex("^1[3-9]\\d{9}$"))) {
            throw ApiException(ResultType.ParamValidationFailed, "手机号码格式不正确")
        }

        userService.changePhone(SecurityContextUtils.userId, dto)
        return mapOf("status" to "success")
    }

    @NormalUser
    @Operation(summary = "更新个人资料")
    @PutMapping("")
    fun updateProfile(@Valid @RequestBody userProfile: User) : User {
        return userService.updateUser(SecurityContextUtils.userId, userProfile)
    }

    @NormalUser
    @Operation(summary = "上传头像")
    @PostMapping("/avatar")
    fun uploadAvatar(@RequestParam avatar: MultipartFile) : User {
        return userService.updateUserAvatar(SecurityContextUtils.userId, avatar)
    }
}