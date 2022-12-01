package cn.net.ziqiang.teamup.backend.common.annotation

import org.springframework.stereotype.Component


@MustBeDocumented
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@Component
annotation class EsClass(
    /**
     * 索引: 前缀index +   默认类名( 自动转小写)
     * 默认类名可以修改为我们自定义
     * @return
     */
    val index: String = "",
    /**
     * 别名: 前缀alias + 默认类名( 自动转小写)
     * 默认类名可以修改为我们自定义
     * @return
     */
    val alias: String = "",
    /**
     *  分片
     */
    val shards: Int = 1,
    /**
     * 分片副本
     * @return
     */
    val replicas: Int = 1,
)

