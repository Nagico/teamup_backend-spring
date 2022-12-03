package cn.net.ziqiang.teamup.backend.controller

import cn.net.ziqiang.teamup.backend.constant.type.ResultType
import cn.net.ziqiang.teamup.backend.constant.UserRole
import cn.net.ziqiang.teamup.backend.pojo.exception.ApiException
import cn.net.ziqiang.teamup.backend.service.UserService
import cn.net.ziqiang.teamup.backend.util.annotation.user.NormalUser
import cn.net.ziqiang.teamup.backend.util.security.SecurityContextUtils
import cn.net.ziqiang.teamup.backend.pojo.vo.test.TestVO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping("/test")
class TestController {
    @Autowired
    private lateinit var userService: UserService

    @GetMapping
    fun testAnonymous(): TestVO {
        try {
            val user = userService.getUserById(id = SecurityContextUtils.userId)
            return TestVO(user.id, user.username, user.role)
        }
        catch (e: ApiException) {
            if (e.code == ResultType.NotLogin.code) {
                return TestVO(null, null, UserRole.None)
            }
            throw e
        }
    }

    @GetMapping("/healthcheck")
    fun healthCheck(): String {
        return ""
    }
    
    @NormalUser
    @GetMapping("users")
    fun testUser(): TestVO {
        val user = userService.getUserById(id = SecurityContextUtils.userId)

        return TestVO(user.id, user.username, user.role)
    }
}