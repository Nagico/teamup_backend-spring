package cn.net.ziqiang.teamup.backend.service.impl

import cn.net.ziqiang.teamup.backend.util.annotation.Slf4j
import cn.net.ziqiang.teamup.backend.util.annotation.Slf4j.Companion.logger
import cn.net.ziqiang.teamup.backend.pojo.auth.JwtPayload
import cn.net.ziqiang.teamup.backend.pojo.auth.TokenBean
import cn.net.ziqiang.teamup.backend.constant.FileConstant.DEFAULT_AVATAR
import cn.net.ziqiang.teamup.backend.constant.type.JwtType
import cn.net.ziqiang.teamup.backend.constant.type.ResultType
import cn.net.ziqiang.teamup.backend.constant.UserRole
import cn.net.ziqiang.teamup.backend.pojo.entity.User
import cn.net.ziqiang.teamup.backend.pojo.exception.ApiException
import cn.net.ziqiang.teamup.backend.util.JwtUtils
import cn.net.ziqiang.teamup.backend.util.SecurityUtils
import cn.net.ziqiang.teamup.backend.dao.repository.UserRepository
import cn.net.ziqiang.teamup.backend.cache.AuthCacheManager
import cn.net.ziqiang.teamup.backend.util.properties.JwtProperties
import cn.net.ziqiang.teamup.backend.service.AuthService
import cn.net.ziqiang.teamup.backend.service.SmsService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Slf4j
@Service
class AuthServiceImpl: cn.net.ziqiang.teamup.backend.service.AuthService {
    @Autowired
    private lateinit var userRepository: UserRepository
    @Autowired
    private lateinit var jwtProperties: JwtProperties
    @Autowired
    private lateinit var authCacheManager: AuthCacheManager
    @Autowired
    private lateinit var smsService: cn.net.ziqiang.teamup.backend.service.SmsService

    //region implementation
    override fun loginWechat(code: String, iv: String, encryptedString: String): cn.net.ziqiang.teamup.backend.pojo.auth.TokenBean {
        //从微信获取资料
//        val sessionKey = wechatServiceManager.getSessionKey(code = code)
//        val decryptedJson =
//            wechatServiceManager.decryptData(sessionKey = sessionKey, iv = iv, encryptedString = encryptedString)

        val decryptedJson = mapOf("openid" to "test")

        return loginOpenid(openid = decryptedJson["openid"].toString())
    }

    override fun loginOpenid(openid: String): cn.net.ziqiang.teamup.backend.pojo.auth.TokenBean {
        var user = userRepository.findByOpenid(openid = openid)

        if (user == null) {
            user = User(
                openid = openid,
                role = UserRole.User,
                avatar = DEFAULT_AVATAR,
                active = false,
                blocked = false,
                createTime = Date(),
            )

            //更新资料
            userRepository.save(user)

            logger.info("新用户: $user")
        }

        //返回新的token
        return generateToken(user)
    }

    override fun loginPassword(phone: String, password: String): cn.net.ziqiang.teamup.backend.pojo.auth.TokenBean {
        val user = userRepository.findByPhone(phone = phone)
            ?: throw ApiException(ResultType.UsernameNotExist, "手机号不存在")

        if (!SecurityUtils.matches(password = password, encodedPassword = user.password)) {
            throw ApiException(ResultType.PasswordWrong)
        }

        //返回新的token
        return generateToken(user)
    }

    override fun refreshToken(refreshToken: String): cn.net.ziqiang.teamup.backend.pojo.auth.TokenBean {
        authCacheManager.getRefreshToken(refreshToken = refreshToken)
            ?: throw ApiException(ResultType.TokenInvalid)

        //验证成功后立刻删掉
        authCacheManager.deleteRefreshToken(refreshToken = refreshToken)

        //解析jwt
        val jwtPayloadInfo =
            JwtUtils.parseJwtWithoutThrow(secretKey = jwtProperties.secret, jwtStr = refreshToken)
        val jwtPayload = jwtPayloadInfo.first ?: throw jwtPayloadInfo.second!!

        // 查询用户信息
        val user = userRepository.findById(jwtPayload.userId).orElse(null) ?: throw ApiException(ResultType.TokenInvalid)

        //提取信息
        val newTokenBean = generateToken(userId = jwtPayload.userId, role = jwtPayload.role, username = jwtPayload.username)

        newTokenBean.user = user

        authCacheManager.setToken(userId = jwtPayload.userId, tokenBean = newTokenBean)

        return newTokenBean
    }

    override fun getAuthToken(userId: Long): String? {
        return authCacheManager.getAuthToken(userId = userId)
    }

    //endregion

    //region utils

    fun generateToken(userId: Long, role: UserRole, username: String): cn.net.ziqiang.teamup.backend.pojo.auth.TokenBean {
        val authPayload = cn.net.ziqiang.teamup.backend.pojo.auth.JwtPayload(
            userId = userId,
            role = role,
            jwtType = JwtType.Auth,
            username = username
        )
        val bearerToken = JwtUtils.generateJwt(
            secretKey = jwtProperties.secret,
            expireOffset = 8 * 60,
            payload = authPayload
        )

        val refreshPayload = cn.net.ziqiang.teamup.backend.pojo.auth.JwtPayload(
            userId = userId,
            role = role,
            jwtType = JwtType.Refresh,
            username = username
        )
        val refreshToken = JwtUtils.generateJwt(
            secretKey = jwtProperties.secret,
            expireOffset = 24 * 60 * 14,
            payload = refreshPayload
        )

        return cn.net.ziqiang.teamup.backend.pojo.auth.TokenBean(access = bearerToken, refresh = refreshToken)
    }

    override fun generateToken(user: User): cn.net.ziqiang.teamup.backend.pojo.auth.TokenBean {
        return generateToken(userId = user.id!!, role = user.role, username = user.username).apply {
            this.user = user
            authCacheManager.setToken(userId = user.id!!, tokenBean = this)
        }
    }

    //endregion
}