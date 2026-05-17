package com.biblioteca.exception;

import com.biblioteca.config.TestcontainersConfig;
import com.biblioteca.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Import(TestcontainersConfig.class)
class GlobalExceptionHandlerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private JwtService jwtService;

    private MockMvc mockMvc;
    private String token;
    private String usuarioId = "673a1b2c3d4e5f6a7b8c9d0e";

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        token = jwtService.generateToken(usuarioId, "teste@email.com");
    }

    @Test
    void deveRetornar404QuandoLivroNaoExistir() throws Exception {
        mockMvc.perform(get("/api/livros/id-inexistente-123")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isNotFound());
    }
}