package cn.net.ziqiang.teamup.backend.service.service

interface SmsService {
    /**
     * 发送验证码
     *
     * @param phone
     */
    fun sendVerifyCode(phone: String)

    /**
     * 校验验证码
     *
     * @param phone
     * @param code
     * @return
     */
    fun checkVerifyCode(phone: String, code: String): Boolean
}