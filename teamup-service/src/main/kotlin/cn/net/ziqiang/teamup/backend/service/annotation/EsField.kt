package cn.net.ziqiang.teamup.backend.service.annotation

import cn.net.ziqiang.teamup.backend.service.constant.EsDataType
import org.springframework.stereotype.Component


@MustBeDocumented
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@Component
annotation class EsField(
    val name: String = "",  //默认属性名
    val type: EsDataType,  //数据类型
    val analyzer: String = "",  //分词
    val searchAnalyzer: String = "",  //搜索分词
)

