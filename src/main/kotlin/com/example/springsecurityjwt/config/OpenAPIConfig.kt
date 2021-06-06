package com.example.springsecurityjwt.config

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.security.SecurityScheme
import io.swagger.v3.oas.models.security.SecurityScheme.Type.HTTP
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OpenAPIConfig {
    @Bean
    fun customOpenAPI(): OpenAPI = OpenAPI()
        .components(
            Components()
                .addSecuritySchemes(
                    "bearer-key",
                    SecurityScheme().type(HTTP).scheme("bearer").bearerFormat("JWT")
                )
        )
}
