package cn.net.ziqiang.teamup.backend.controller

import cn.net.ziqiang.teamup.backend.constant.type.ResultType
import cn.net.ziqiang.teamup.backend.pojo.entity.Competition
import cn.net.ziqiang.teamup.backend.pojo.entity.Team
import cn.net.ziqiang.teamup.backend.pojo.entity.TeamRole
import cn.net.ziqiang.teamup.backend.pojo.exception.ApiException
import cn.net.ziqiang.teamup.backend.pojo.entity.User
import cn.net.ziqiang.teamup.backend.pojo.vo.auth.AuthVO
import cn.net.ziqiang.teamup.backend.pojo.vo.user.*
import cn.net.ziqiang.teamup.backend.service.RecommendService
import cn.net.ziqiang.teamup.backend.service.UserService
import cn.net.ziqiang.teamup.backend.util.annotation.user.NormalUser
import cn.net.ziqiang.teamup.backend.util.security.SecurityContextUtils
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
    @Autowired
    private lateinit var recommendService: RecommendService

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
    fun uploadAvatar(@RequestParam("file") avatar: MultipartFile) : User {
        return userService.updateUserAvatar(SecurityContextUtils.userId, avatar)
    }

    @NormalUser
    @Operation(summary = "获取用户订阅比赛列表")
    @GetMapping("/subscriptions/competitions/")
    fun getSubscriptions() : List<Competition> {
        return recommendService.getUserSubscribeCompetition(SecurityContextUtils.userId)
    }

    @NormalUser
    @Operation(summary = "添加比赛订阅")
    @PostMapping("/subscriptions/competitions/{id}")
    fun addSubscription(@PathVariable id: Long) : Map<String, String> {
        recommendService.addUserSubscribeCompetition(SecurityContextUtils.userId, id)
        return mapOf("status" to "success")
    }

    @NormalUser
    @Operation(summary = "删除比赛订阅")
    @DeleteMapping("/subscriptions/competitions/{id}")
    fun deleteSubscription(@PathVariable id: Long) : Map<String, String> {
        recommendService.deleteUserSubscribeCompetition(SecurityContextUtils.userId, id)
        return mapOf("status" to "success")
    }

    @NormalUser
    @Operation(summary = "获取用户订阅角色列表")
    @GetMapping("/subscriptions/roles/")
    fun getRoleSubscriptions() : List<TeamRole> {
        return recommendService.getUserSubscribeRole(SecurityContextUtils.userId)
    }

    @NormalUser
    @Operation(summary = "添加角色订阅")
    @PostMapping("/subscriptions/roles/{id}")
    fun addRoleSubscription(@PathVariable id: Long) : Map<String, String> {
        recommendService.addUserSubscribeRole(SecurityContextUtils.userId, id)
        return mapOf("status" to "success")
    }

    @NormalUser
    @Operation(summary = "删除角色订阅")
    @DeleteMapping("/subscriptions/roles/{id}")
    fun deleteRoleSubscription(@PathVariable id: Long) : Map<String, String> {
        recommendService.deleteUserSubscribeRole(SecurityContextUtils.userId, id)
        return mapOf("status" to "success")
    }

    @NormalUser
    @Operation(summary = "获取用户收藏队伍列表")
    @GetMapping("/favorites/teams/")
    fun getTeamFavorites() : Set<Team> {
        return recommendService.getUserFavoriteTeam(SecurityContextUtils.userId)
    }

    @NormalUser
    @Operation(summary = "收藏队伍")
    @PostMapping("/favorites/teams/{id}")
    fun addTeamFavorite(@PathVariable id: Long) : Map<String, String> {
        recommendService.addUserFavoriteTeam(SecurityContextUtils.userId, id)
        return mapOf("status" to "success")
    }

    @NormalUser
    @Operation(summary = "取消收藏队伍")
    @DeleteMapping("/favorites/teams/{id}")
    fun deleteTeamFavorite(@PathVariable id: Long) : Map<String, String> {
        recommendService.deleteUserFavoriteTeam(SecurityContextUtils.userId, id)
        return mapOf("status" to "success")
    }

    @NormalUser
    @Operation(summary = "队伍感兴趣")
    @PostMapping("/interests/teams/{id}")
    fun addTeamInterest(@PathVariable id: Long) : Map<String, String> {
        recommendService.addUserInterestingTeam(SecurityContextUtils.userId, id)
        return mapOf("status" to "success")
    }

    @NormalUser
    @Operation(summary = "取消队伍感兴趣")
    @DeleteMapping("/interests/teams/{id}")
    fun deleteTeamInterest(@PathVariable id: Long) : Map<String, String> {
        recommendService.deleteUserInterestingTeam(SecurityContextUtils.userId, id)
        return mapOf("status" to "success")
    }

    @NormalUser
    @Operation(summary = "队伍不感兴趣")
    @PostMapping("/disinterests/teams/{id}")
    fun addTeamDisinterest(@PathVariable id: Long) : Map<String, String> {
        recommendService.addUserUninterestingTeam(SecurityContextUtils.userId, id)
        return mapOf("status" to "success")
    }

    @NormalUser
    @Operation(summary = "取消队伍不感兴趣")
    @DeleteMapping("/disinterests/teams/{id}")
    fun deleteTeamDisinterest(@PathVariable id: Long) : Map<String, String> {
        recommendService.deleteUserUninterestingTeam(SecurityContextUtils.userId, id)
        return mapOf("status" to "success")
    }
}