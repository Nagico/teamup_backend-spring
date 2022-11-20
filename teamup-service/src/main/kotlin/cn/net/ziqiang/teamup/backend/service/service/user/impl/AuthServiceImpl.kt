package cn.net.ziqiang.teamup.backend.service.service.user.impl

import cn.net.ziqiang.teamup.backend.common.bean.auth.JwtPayload
import cn.net.ziqiang.teamup.backend.common.bean.auth.TokenBean
import cn.net.ziqiang.teamup.backend.common.constant.JwtType
import cn.net.ziqiang.teamup.backend.common.constant.ResultType
import cn.net.ziqiang.teamup.backend.common.constant.UserRole
import cn.net.ziqiang.teamup.backend.common.entity.User
import cn.net.ziqiang.teamup.backend.common.exception.ApiException
import cn.net.ziqiang.teamup.backend.common.utils.auth.JwtUtils
import cn.net.ziqiang.teamup.backend.dao.repository.UserRepository
import cn.net.ziqiang.teamup.backend.service.cache.AuthCenterCacheManager
import cn.net.ziqiang.teamup.backend.service.properties.JwtProperties
import cn.net.ziqiang.teamup.backend.service.service.user.AuthService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class AuthServiceImpl: AuthService {
//    @Autowired
//    lateinit var wechatServiceManager: WechatServiceManager
    @Autowired
    lateinit var userRepository: UserRepository
    @Autowired
    lateinit var jwtProperties: JwtProperties
    @Autowired
    lateinit var authCenterCacheManager: AuthCenterCacheManager

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
            user = User()
        }

        //更新资料
        userRepository.save(user)

        val generatedToken = generateToken(user)
        authCenterCacheManager.setToken(userId = user.id!!, tokenBean = generatedToken)

        //返回新的token
        return generatedToken
    }

    override fun refreshToken(refreshToken: String): TokenBean {
        authCenterCacheManager.getRefreshToken(refreshToken = refreshToken)
            ?: throw ApiException(ResultType.TokenInvalid)

        //验证成功后立刻删掉
        authCenterCacheManager.deleteRefreshToken(refreshToken = refreshToken)

        //解析jwt
        val jwtPayloadInfo =
            JwtUtils.parseJwtWithoutThrow(secretKey = jwtProperties.secret, jwtStr = refreshToken)
        val jwtPayload = jwtPayloadInfo.first ?: throw jwtPayloadInfo.second!!

        //提取信息
        val newTokenBean = generateToken(userId = jwtPayload.userId, role = jwtPayload.role)
        authCenterCacheManager.setToken(userId = jwtPayload.userId, tokenBean = newTokenBean)
        return newTokenBean
    }

    override fun getAuthToken(userId: Long): String? {
        return authCenterCacheManager.getAuthToken(userId = userId)
    }

    //endregion

    //region utils

    fun generateToken(userId: Long, role: UserRole): TokenBean {
        val authPayload = JwtPayload(userId = userId, role =role, jwtType = JwtType.Auth)
        val bearerToken = "bearer " + JwtUtils.generateJwt(
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

        return TokenBean(auth = bearerToken, refresh = refreshToken)
    }

    fun generateToken(user: User): TokenBean {
        return generateToken(userId = user.id!!, role = user.role)
    }

    //endregion
}