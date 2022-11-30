package cn.net.ziqiang.teamup.backend.service.constant


object RedisKey {
    //region user

    fun userKey(userId: Long) = "user::userId$userId"

    fun userStatusKey(userId: Long) = "user.userStatus::userId$userId"

    //endregion

    //region auth

    fun authTokenKey(userId: Long) = "auth.authToken::userId=$userId"

    fun refreshToken(refreshToken: String) = "auth.refreshToken::refreshToken=$refreshToken"
    //endregion

    // region sms
    fun smsVerifyCodeKey(phone: String) = "sms.smsVerifyCode::phone=$phone"

    fun phoneStatusKey(phone: String) = "sms.phoneStatus::phone=$phone"
    // endregion

    // region recruitment

    fun recruitmentKey(id: Long): String = "recruitment.recruitment::id=$id"

    // endregion
}