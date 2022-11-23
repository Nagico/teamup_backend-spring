package cn.net.ziqiang.teamup.backend.web.config

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.info.License
import io.swagger.v3.oas.models.security.SecurityScheme
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
    }

}