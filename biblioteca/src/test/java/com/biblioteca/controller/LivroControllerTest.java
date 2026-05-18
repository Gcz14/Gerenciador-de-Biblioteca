package com.biblioteca.controller;

import com.biblioteca.dto.LivroRequestDTO;
import com.biblioteca.repository.LivroRepository;
import com.biblioteca.security.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
class LivroControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private LivroRepository livroRepository;

    @Autowired
    private JwtService jwtService;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();
    private String token;
    private String usuarioId = "673a1b2c3d4e5f6a7b8c9d0e";

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        livroRepository.deleteAll();
        token = jwtService.generateToken(usuarioId, "teste@email.com");
    }

    @Test
    void deveCriarLivro() throws Exception {
        LivroRequestDTO dto = new LivroRequestDTO();
        dto.setTitulo("Livro Teste");
        dto.setAutor("Autor Teste");
        dto.setStatus("DISPONIVEL");

        mockMvc.perform(post("/api/livros")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.titulo").value("Livro Teste"));
    }
}