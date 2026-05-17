package com.biblioteca.config;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TestcontainersConfig {

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        // Usando o MongoDB que já está rodando
        registry.add("spring.data.mongodb.uri", () -> "mongodb://localhost:27017/biblioteca_test");
    }
}