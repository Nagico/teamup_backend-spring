package cn.net.ziqiang.teamup.backend.util.annotation

import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class Slf4j{
    companion object{
        val <reified T> T.logger: Logger
            inline get() = LoggerFactory.getLogger(T::class.java)
    }
}
