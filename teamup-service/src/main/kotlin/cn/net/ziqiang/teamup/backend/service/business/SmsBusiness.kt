package cn.net.ziqiang.teamup.backend.service.business

interface SmsBusiness {
    /**
     * 发送验证码短信
     *
     * @param phone
     * @param code
     */
    fun sendVerifyCode(phone: String, code: String)
}