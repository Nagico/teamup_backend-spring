package cn.net.ziqiang.teamup.backend.service.service.impl

import cn.net.ziqiang.teamup.backend.common.annotation.Slf4j
import cn.net.ziqiang.teamup.backend.common.annotation.Slf4j.Companion.logger
import cn.net.ziqiang.teamup.backend.common.pojo.auth.JwtPayload
import cn.net.ziqiang.teamup.backend.common.pojo.auth.TokenBean
import cn.net.ziqiang.teamup.backend.common.constant.FileConstant.DEFAULT_AVATAR
import cn.net.ziqiang.teamup.backend.common.constant.type.JwtType
import cn.net.ziqiang.teamup.backend.common.constant.type.ResultType
import cn.net.ziqiang.teamup.backend.common.constant.UserRole
import cn.net.ziqiang.teamup.backend.common.pojo.entity.User
import cn.net.ziqiang.teamup.backend.common.exception.ApiException
import cn.net.ziqiang.teamup.backend.common.util.JwtUtils
import cn.net.ziqiang.teamup.backend.common.util.SecurityUtils
import cn.net.ziqiang.teamup.backend.dao.repository.UserRepository
import cn.net.ziqiang.teamup.backend.service.cache.AuthCacheManager
import cn.net.ziqiang.teamup.backend.service.properties.JwtProperties
import cn.net.ziqiang.teamup.backend.service.service.AuthService
import cn.net.ziqiang.teamup.backend.service.service.SmsService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Slf4j
@Service
class AuthServiceImpl: AuthService {
    @Autowired
    private lateinit var userRepository: UserRepository
    @Autowired
    private lateinit var jwtProperties: JwtProperties
    @Autowired
    private lateinit var authCacheManager: AuthCacheManager
    @Autowired
    private lateinit var smsService: SmsService

    //region implementation
    override fun loginWechat(code: String, iv: String, encryptedString: String): TokenBean {
        //从微信获取资料
//        val sessionKey = wechatServiceManager.getSessionKey(code = code)
//        val decryptedJson =
//            wechatServiceManager.decryptData(sessionKey = sessionKey, iv = iv, encryptedString = encryptedString)

        val decryptedJson = mapOf("openid" to "test")

        return loginOpenid(openid = decryptedJson["openid"].toString())
    }

    override fun loginOpenid(openid: String): TokenBean {
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

    override fun loginPassword(phone: String, password: String): TokenBean {
        val user = userRepository.findByPhone(phone = phone)
            ?: throw ApiException(ResultType.UsernameNotExist, "手机号不存在")

        if (!SecurityUtils.matches(password = password, encodedPassword = user.password)) {
            throw ApiException(ResultType.PasswordWrong)
        }

        //返回新的token
        return generateToken(user)
    }

    override fun refreshToken(refreshToken: String): TokenBean {
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
        val newTokenBean = generateToken(userId = jwtPayload.userId, role = jwtPayload.role)

        newTokenBean.user = user

        authCacheManager.setToken(userId = jwtPayload.userId, tokenBean = newTokenBean)

        return newTokenBean
    }

    override fun getAuthToken(userId: Long): String? {
        return authCacheManager.getAuthToken(userId = userId)
    }

    //endregion

    //region utils

    fun generateToken(userId: Long, role: UserRole): TokenBean {
        val authPayload = JwtPayload(userId = userId, role =role, jwtType = JwtType.Auth)
        val bearerToken = JwtUtils.generateJwt(
            secretKey = jwtProperties.secret,
            expireOffset = 8 * 60,
            payload = authPayload
        )

        val refreshPayload = JwtPayload(userId = userId, role = role, jwtType = JwtType.Refresh)
        val refreshToken = JwtUtils.generateJwt(
            secretKey = jwtProperties.secret,
            expireOffset = 24 * 60 * 14,
            payload = refreshPayload
        )

        return TokenBean(access = bearerToken, refresh = refreshToken)
    }

    override fun generateToken(user: User): TokenBean {
        return generateToken(userId = user.id!!, role = user.role).apply {
            this.user = user
            authCacheManager.setToken(userId = user.id!!, tokenBean = this)
        }
    }

    //endregion
}