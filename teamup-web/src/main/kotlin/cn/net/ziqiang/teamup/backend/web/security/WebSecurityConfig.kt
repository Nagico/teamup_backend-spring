package cn.net.ziqiang.teamup.backend.web.security

import cn.net.ziqiang.teamup.backend.service.properties.JwtProperties
import cn.net.ziqiang.teamup.backend.service.service.AuthService
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource


/**
 * SpringSecurity配置类
 * 下面有个坑。虽然WebSecurityConfigurerAdapter过时了，但用推荐但方式注入AuthenticationManager会导致
 * 含ApplicationContext的单测不通过
 */
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Configuration
class WebSecurityConfig(
    private val failHandler: AuthenticationFailHandler,
    private val jwtProperties: JwtProperties,
    private val authService: AuthService
): WebSecurityConfigurerAdapter() {

    //region override

    override fun configure(web: WebSecurity?) {
        super.configure(web)
    }

    override fun configure(http: HttpSecurity) {
        //跨域
        http.cors().configurationSource(corsConfigurationSource())

        //禁用csrf
        http.csrf().disable()

        //验证过滤器
        http.addFilterBefore(
            /* filter = */ jwtAuthenticationFilter(
                authenticationManager = authenticationManager(),
                jwtSecret = jwtProperties.secret
            ),
            /* beforeFilter = */ BasicAuthenticationFilter::class.java
        )

        //配置路径
        http.authorizeRequests()
            .antMatchers("/**").permitAll()

        //失败处理
        http.exceptionHandling()
            .authenticationEntryPoint(failHandler)
            .accessDeniedHandler(failHandler)
    }

    override fun configure(auth: AuthenticationManagerBuilder?) {
        super.configure(auth)
    }

    //endregion

    fun corsConfigurationSource(): CorsConfigurationSource {
        val corsConfiguration = CorsConfiguration().apply {
//            allowedHeaders = listOf("*")
//            allowedMethods = listOf("*")
            allowedOrigins = listOf("http://localhost:5500", "http://jxy.me")
            maxAge = 3600
            allowCredentials = true
        }
        val source = UrlBasedCorsConfigurationSource().apply {
            registerCorsConfiguration("/**", corsConfiguration)
        }
        return source
    }

    fun jwtAuthenticationFilter(
        authenticationManager: AuthenticationManager,
        jwtSecret: String
    ): JwtAuthenticationFilter {
        return JwtAuthenticationFilter(
            authenticationManager = authenticationManager,
            jwtSecret = jwtSecret,
            authService = authService
        )
    }
}