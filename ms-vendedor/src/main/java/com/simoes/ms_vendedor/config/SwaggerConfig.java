package com.simoes.ms_pedido.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.OpenAPI;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("MarketFlow API")
                        .version("1.0")
                        .description("API para processamento de pedidos em marketplace utilizando RabbitMQ"));
    }
}