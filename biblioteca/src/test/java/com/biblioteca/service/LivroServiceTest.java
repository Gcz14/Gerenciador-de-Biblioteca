package com.biblioteca.service;

import com.biblioteca.config.TestcontainersConfig;
import com.biblioteca.dto.LivroRequestDTO;
import com.biblioteca.dto.LivroResponseDTO;
import com.biblioteca.dto.UserRequestDTO;
import com.biblioteca.dto.UserResponseDTO;
import com.biblioteca.repository.LivroRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class LivroServiceTest extends TestcontainersConfig {

    @Autowired
    private LivroService livroService;

    @Autowired
    private UserService userService;

    @Autowired
    private LivroRepository livroRepository;

    private String usuarioId;

    @BeforeEach
    void setUp() {
        livroRepository.deleteAll();
        
        UserRequestDTO userDto = new UserRequestDTO("Dono Livro", "dono@email.com", "123456");
        UserResponseDTO user = userService.create(userDto);
        usuarioId = user.id();
    }

    @Test
    void deveCriarLivroComSucesso() {
        LivroRequestDTO dto = new LivroRequestDTO();
        dto.setTitulo("Dom Casmurro");
        dto.setAutor("Machado de Assis");
        dto.setIsbn("9788575421234");
        dto.setAnoPublicacao(1899);
        dto.setCategoria("Ficção");
        dto.setStatus("DISPONIVEL");

        LivroResponseDTO response = livroService.criarLivro(dto, usuarioId);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isNotNull();
        assertThat(response.getTitulo()).isEqualTo("Dom Casmurro");
        assertThat(response.getUsuarioId()).isEqualTo(usuarioId);
    }
}