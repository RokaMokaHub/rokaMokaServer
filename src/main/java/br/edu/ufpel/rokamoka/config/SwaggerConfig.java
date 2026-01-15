package br.edu.ufpel.rokamoka.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityScheme.Type;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        SecurityScheme basicScheme = new SecurityScheme().type(Type.HTTP)
                .description("Login com credencias do usuário")
                .scheme("basic");
        SecurityScheme bearerScheme = new SecurityScheme().type(Type.HTTP)
                .description("Autenticação via token")
                .scheme("bearer");
        return new OpenAPI().components(
                        new Components().addSecuritySchemes("basic", basicScheme)
                                .addSecuritySchemes("bearer", bearerScheme))
                .addSecurityItem(new SecurityRequirement().addList("bearer"));
    }
}
