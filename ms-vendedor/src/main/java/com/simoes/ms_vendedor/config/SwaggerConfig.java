package com.simoes.ms_vendedor.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("MarketFlow API")
                        .version("1.0")
                        .description("API para processamento de pedidos em marketplace utilizando RabbitMQ (Microsservico - Vendedor)")
                );
    }
}