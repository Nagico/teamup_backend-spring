package cn.net.ziqiang.teamup.backend.web.security

import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.core.GrantedAuthority

class JwtAuthenticationToken(authorities: List<GrantedAuthority>): AbstractAuthenticationToken(authorities) {
    init {
        super.setAuthenticated(true)
    }
    override fun getCredentials(): Any? {
        return null
    }

    override fun getPrincipal(): Any {
        return true
    }
}