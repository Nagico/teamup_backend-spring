package cn.net.ziqiang.teamup.backend.util.annotation

import org.springframework.stereotype.Component

@MustBeDocumented
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@Component
annotation class DocId()
