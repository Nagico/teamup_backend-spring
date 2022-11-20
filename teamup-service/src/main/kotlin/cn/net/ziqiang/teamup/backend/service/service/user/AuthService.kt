package cn.net.ziqiang.teamup.backend.service.service.user

import cn.net.ziqiang.teamup.backend.common.bean.auth.TokenBean


interface AuthService {
    /**
     * 微信登录
     * @param code
     * @param encryptedString
     * @return token
     */
    fun loginWechat(code: String, iv: String, encryptedString: String): TokenBean

    /**
     * 刷新token
     * @param refreshToken
     * @return
     */
    fun refreshToken(refreshToken: String): TokenBean
    fun getAuthToken(userId: Long): String?
}