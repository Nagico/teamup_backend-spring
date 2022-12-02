package cn.net.ziqiang.teamup.backend.util

import cn.hutool.core.bean.BeanUtil
import cn.hutool.core.date.DateField
import cn.hutool.core.date.DateTime
import cn.hutool.json.JSONUtil
import cn.hutool.jwt.JWTPayload
import cn.hutool.jwt.JWTUtil
import cn.net.ziqiang.teamup.backend.constant.type.ResultType
import cn.net.ziqiang.teamup.backend.pojo.exception.ApiException
import cn.net.ziqiang.teamup.backend.pojo.auth.JwtPayload


object JwtUtils {

    /**
     * 解析jwt
     * @param secretKey
     * @param jwtStr
     * @return
     */
    fun parseJwt(secretKey: String = "", jwtStr: String): cn.net.ziqiang.teamup.backend.pojo.auth.JwtPayload {
        val jwtInfo = parseJwtWithoutThrow(secretKey = secretKey, jwtStr = jwtStr)
        jwtInfo.second?.let {
            throw it
        }
        return jwtInfo.first!!
    }

    /**
     * 解析Jwt，但出现异常不抛出直接返回
     * @param secretKey
     * @param jwtStr
     * @return
     */
    fun parseJwtWithoutThrow(secretKey: String = "", jwtStr: String): Pair<cn.net.ziqiang.teamup.backend.pojo.auth.JwtPayload?, ApiException?> {
        val jwt = try {
            JWTUtil.parseToken(jwtStr)
        } catch (e: Exception) {
            return Pair(null, ApiException(type = ResultType.NotLogin))
        }

        if (secretKey.isNotEmpty() && !jwt.setKey(secretKey.toByteArray()).verify()) {
            return Pair(null, ApiException(type = ResultType.NotLogin))
        }

        val payload = JSONUtil.toBean(jwt.payloads, cn.net.ziqiang.teamup.backend.pojo.auth.JwtPayload::class.java)
        if (!jwt.validate(0)) {
            return Pair(payload, ApiException(type = ResultType.TokenInvalid))
        }

        return Pair(payload, null)
    }

    /**
     * 生成jwt
     * @param secretKey 密钥
     * @param expireOffset 过期间隔，单位为分钟
     * @param payload 载体
     * @return
     */
    fun generateJwt(secretKey: String, expireOffset: Int, payload: cn.net.ziqiang.teamup.backend.pojo.auth.JwtPayload): String {
        val payloadMap = BeanUtil.beanToMap(payload)
        with(DateTime.now()) {
            payloadMap[JWTPayload.ISSUED_AT] = this
            payloadMap[JWTPayload.EXPIRES_AT] = this.offsetNew(DateField.MINUTE, expireOffset)
            payloadMap[JWTPayload.NOT_BEFORE] = this
        }

        return JWTUtil.createToken(payloadMap, secretKey.toByteArray())
    }
}