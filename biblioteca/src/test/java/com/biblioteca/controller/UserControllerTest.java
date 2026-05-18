package com.biblioteca.controller;

import com.biblioteca.config.TestcontainersConfig;
import com.biblioteca.dto.UserRequestDTO;
import com.biblioteca.model.User;
import com.biblioteca.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
class UserControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private MockMvc mockMvc;
    private String usuarioId;
    private String email = "teste@email.com";
    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        userRepository.deleteAll();
        
        User user = new User();
        user.setNome("Usuario Teste");
        user.setEmail(email);
        user.setSenha(passwordEncoder.encode("senha123"));
        User saved = userRepository.save(user);
        usuarioId = saved.getId();
    }

    @Test
    void deveCriarUsuario() throws Exception {
        UserRequestDTO dto = new UserRequestDTO("Novo Usuario", "novo@email.com", "senha123");

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value("Novo Usuario"))
                .andExpect(jsonPath("$.email").value("novo@email.com"));
    }

    @Test
    void deveListarUsuarios() throws Exception {
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void deveBuscarUsuarioPorId() throws Exception {
        mockMvc.perform(get("/api/users/" + usuarioId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(email));
    }

    @Test
    void deveAtualizarUsuario() throws Exception {
        UserRequestDTO dto = new UserRequestDTO("Nome Atualizado", email, "novaSenha123");

        mockMvc.perform(put("/api/users/" + usuarioId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Nome Atualizado"));
    }

    @Test
    void deveDeletarUsuario() throws Exception {
        mockMvc.perform(delete("/api/users/" + usuarioId))
                .andExpect(status().isNoContent());
    }

    @Test
    void deveRetornar404AoBuscarUsuarioInexistente() throws Exception {
        mockMvc.perform(get("/api/users/id-inexistente-123"))
                .andExpect(status().isNotFound());
    }
}