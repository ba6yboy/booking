package com.example.booking.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Booking System API")
                        .version("1.0.0")
                        .description("API для автоматизованої системи бронювання послуг.")
                        .contact(new Contact()
                                .name("Andrii")
                                .email("ch.andrey.14@gmail.com")
                                .url("https://github.com/ba6yboy")
                        )
                );
    }
}