package cn.net.ziqiang.teamup.backend.service.annotation

import org.springframework.stereotype.Service

@Target(AnnotationTarget.TYPE, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
@Service
annotation class Business