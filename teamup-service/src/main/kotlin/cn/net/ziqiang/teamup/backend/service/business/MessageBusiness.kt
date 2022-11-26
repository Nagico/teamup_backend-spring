package cn.net.ziqiang.teamup.backend.service.business

import cn.net.ziqiang.teamup.backend.common.pojo.entity.User

interface MessageBusiness {
    fun userLogin(userId: Long) : User
    
    fun userLogout(user: User)
}