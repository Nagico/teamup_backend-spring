package cn.net.ziqiang.teamup.backend.service.config.websocket

import java.security.Principal


class UserPrincipal(private val name: String) : Principal {
    override fun getName(): String {
        return name
    }
}