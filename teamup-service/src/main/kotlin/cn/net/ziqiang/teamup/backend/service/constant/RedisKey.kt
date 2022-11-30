package cn.net.ziqiang.teamup.backend.service.constant

import cn.net.ziqiang.teamup.backend.common.pojo.entity.Team


object RedisKey {
    //region user

    fun userKey(userId: Long) = "user:user:userId$userId"

    fun userStatusKey(userId: Long) = "user:userStatus:userId$userId"

    //endregion

    //region auth

    fun authTokenKey(userId: Long) = "auth:authToken:userId=$userId"

    fun refreshToken(refreshToken: String) = "auth:refreshToken:refreshToken=$refreshToken"
    //endregion

    // region sms
    fun smsVerifyCodeKey(phone: String) = "sms:smsVerifyCode:phone=$phone"

    fun phoneStatusKey(phone: String) = "sms:phoneStatus:phone=$phone"
    // endregion

    // region recruitment

    fun recruitmentKey(id: Long): String = "recruitment:recruitment:id=$id"

    fun recruitmentListByTeamIdKey(teamId: Long) = "recruitment:recruitmentListByTeamId:teamId=$teamId"

    // endregion

    // region team

    fun teamKey(teamId: Long) = "team:team:id=$teamId"

    fun teamListByUserIdKey(userId: Long) = "team:teamListByUserId:userId=$userId"

    fun teamRoleTree(rootId: Long) = "team::teamRoleTree:rootId=$rootId"

    // endregion
}