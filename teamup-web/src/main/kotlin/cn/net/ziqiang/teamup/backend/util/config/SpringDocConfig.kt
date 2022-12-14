package cn.net.ziqiang.teamup.backend.util.config

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.info.License
import io.swagger.v3.oas.models.security.SecurityScheme
import io.swagger.v3.oas.models.servers.Server
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class SpringDocConfig {
    private val securitySchemeName = "JWT Auth"

    @Bean
    fun openApi(): OpenAPI {
        return OpenAPI()
            .info(
                Info()
                    .title("赛道友你API文档")
                    .license(
                        License()
                            .name("AGPL-3.0")
                            .url("https://www.gnu.org/licenses/agpl-3.0.html")
                    )
            )
//            .addSecurityItem(
//                SecurityRequirement()
//                    .addList(securitySchemeName)
//            )
            .components(
                Components()
                    .addSecuritySchemes(securitySchemeName, SecurityScheme()
                        .name(securitySchemeName)
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("Bearer")
                        .bearerFormat("JWT")
                    )
            )
            .servers(
                listOf(
                    if (!System.getenv("HOST_FULL").isNullOrBlank()) {
                        Server().url(System.getenv("HOST_FULL")).description("当前环境")
                    } else {
                        Server().url("http://localhost:8080").description("开发环境")
                    },
                    Server()
                        .url("https://api.teamup.nagico.cn")
                        .description("测试环境"),
                    Server()
                        .url("http://localhost:8080")
                        .description("本地环境")
                )
            )
    }

}