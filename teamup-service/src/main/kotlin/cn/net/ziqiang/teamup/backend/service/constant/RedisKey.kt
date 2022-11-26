package cn.net.ziqiang.teamup.backend.service.constant


object RedisKey {
    //region user

    fun userKey(userId: Long): String {
        return "user::userId$userId"
    }

    fun userStatusKey(userId: Long): String {
        return "user.userStatus::userId$userId"
    }

    //endregion

    //region auth

    fun authTokenKey(userId: Long): String {
        return "auth.authToken::userId=$userId"
    }

    fun refreshToken(refreshToken: String): String {
        return "auth.refreshToken::refreshToken=$refreshToken"
    }
    //endregion

    // region sms
    fun smsVerifyCodeKey(phone: String): String {
        return "sms.smsVerifyCode::phone=$phone"
    }

    fun phoneStatusKey(phone: String): String {
        return "sms.phoneStatus::phone=$phone"
    }
    // endregion
}