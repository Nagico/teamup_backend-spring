package cn.net.ziqiang.teamup.backend.service

import cn.net.ziqiang.teamup.backend.pojo.auth.TokenBean
import cn.net.ziqiang.teamup.backend.pojo.entity.User


interface AuthService {
    /**
     * 微信登录
     * @param code
     * @param encryptedString
     * @return token
     */
    fun loginWechat(code: String, iv: String, encryptedString: String): cn.net.ziqiang.teamup.backend.pojo.auth.TokenBean

    /**
     * 测试 openid直接登录
     *
     * @param openid
     * @return
     */
    fun loginOpenid(openid: String): cn.net.ziqiang.teamup.backend.pojo.auth.TokenBean

    /**
     * 用户名密码登录
     *
     * @param phone
     * @param password
     * @return
     */
    fun loginPassword(phone: String, password: String): cn.net.ziqiang.teamup.backend.pojo.auth.TokenBean

    /**
     * 刷新token
     * @param refreshToken
     * @return
     */
    fun refreshToken(refreshToken: String): cn.net.ziqiang.teamup.backend.pojo.auth.TokenBean

    /**
     * 缓存获取用户token
     *
     * @param userId
     * @return
     */
    fun getAuthToken(userId: Long): String?

    /**
     * 生成token
     *
     * @param user
     * @return
     */
    fun generateToken(user: User): cn.net.ziqiang.teamup.backend.pojo.auth.TokenBean
}