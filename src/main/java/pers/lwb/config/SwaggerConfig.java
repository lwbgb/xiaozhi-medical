package pers.lwb.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI swaggerOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("xiaozhi-medical")
                        .version("1.0")
                        .description("后端 API 文档生成"))
                .externalDocs(new ExternalDocumentation()
                        .description("Vue 前端")
                        .url("http://127.0.0.1:80"));
    }
}
