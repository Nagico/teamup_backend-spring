package cn.net.ziqiang.teamup.backend.common.util

import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder


/** ISO8601时间格式  */
private const val ISO8601_FORMATTER_STR = "yyyy-MM-dd'T'HH:mm:ss.SSSZ"

@Bean
fun jacksonObjectMapperCustomization(): Jackson2ObjectMapperBuilderCustomizer {
    return Jackson2ObjectMapperBuilderCustomizer { jacksonObjectMapperBuilder: Jackson2ObjectMapperBuilder ->
        jacksonObjectMapperBuilder.simpleDateFormat(
            ISO8601_FORMATTER_STR
        )
    }
}
