package cn.net.ziqiang.teamup.backend.service.business.impl

import cn.net.ziqiang.teamup.backend.common.annotation.Slf4j
import cn.net.ziqiang.teamup.backend.common.annotation.Slf4j.Companion.logger
import cn.net.ziqiang.teamup.backend.common.constant.type.ResultType
import cn.net.ziqiang.teamup.backend.common.exception.ApiException
import cn.net.ziqiang.teamup.backend.common.pojo.entity.User
import cn.net.ziqiang.teamup.backend.common.util.JwtUtils
import cn.net.ziqiang.teamup.backend.service.annotation.Business
import cn.net.ziqiang.teamup.backend.service.business.MessageBusiness
import cn.net.ziqiang.teamup.backend.service.properties.JwtProperties
import cn.net.ziqiang.teamup.backend.service.service.UserService
import org.springframework.beans.factory.annotation.Autowired

@Slf4j
@Business
class MessageBusinessImpl : MessageBusiness {
    @Autowired
    private lateinit var jwtProperties: JwtProperties
    @Autowired
    private lateinit var userService: UserService

    override fun userLogin(userId: Long) : User {
        return userService.getUserById(userId).apply {
            logger.info("user login: $name")
            userService.messageLogin(this.id!!)
        }
    }

    override fun userLogout(user: User) {
        logger.info("user logout: ${user.name}")
        userService.messageLogout(user.id!!)
    }
}