package cn.net.ziqiang.teamup.backend.web.config

import cn.net.ziqiang.teamup.backend.common.constant.ResultType
import cn.net.ziqiang.teamup.backend.service.vo.ResultVO
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.media.Content
import io.swagger.v3.oas.models.media.Schema
import io.swagger.v3.oas.models.responses.ApiResponse
import org.springdoc.core.customizers.OpenApiCustomiser
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType


@Configuration
class SpringDocConfig {

    @Bean
    fun openApi(): OpenAPI {
        return OpenAPI()
            .info(
                Info()
                    .title("赛道有你API文档")
            )
    }

    @Bean
    fun apiCustomizer(): OpenApiCustomiser {
        return OpenApiCustomiser {
            it.paths.values.forEach { pathItem ->
                pathItem.readOperations().forEach { operation ->
                    val responses = operation.responses

                    ResultType.values().forEach { resultCode ->
                        val result = ResultVO<Any>(resultCode)
                        val response = ApiResponse()
                            .description("${resultCode.code}-${resultCode.name}")
                            .content(
                                Content()
                                    .addMediaType(MediaType.APPLICATION_JSON_VALUE,
                                        io.swagger.v3.oas.models.media.MediaType()
                                            .schema(
                                                Schema<ResultVO<Any>>()
                                                    .example(result)
                                            )
                                    )
                            )
                        responses.addApiResponse(resultCode.code, response)
                    }
                }
            }
        }
    }
}