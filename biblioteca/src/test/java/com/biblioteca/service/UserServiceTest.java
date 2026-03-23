package com.biblioteca.service;

import com.biblioteca.config.TestcontainersConfig;
import com.biblioteca.dto.UserRequestDTO;
import com.biblioteca.dto.UserResponseDTO;
import com.biblioteca.exception.ResourceNotFoundException;
import com.biblioteca.model.User;
import com.biblioteca.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UserServiceTest extends TestcontainersConfig {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    void deveCriarUsuarioComSucesso() {
        UserRequestDTO dto = new UserRequestDTO("Teste User", "teste@email.com", "123456");

        UserResponseDTO response = userService.create(dto);

        assertThat(response).isNotNull();
        assertThat(response.id()).isNotNull();
        assertThat(response.nome()).isEqualTo("Teste User");
        assertThat(response.email()).isEqualTo("teste@email.com");

        User savedUser = userRepository.findById(response.id()).orElseThrow();
        assertThat(passwordEncoder.matches("123456", savedUser.getSenha())).isTrue();
    }

    @Test
    void deveLancarExcecaoAoCriarUsuarioComEmailDuplicado() {
        UserRequestDTO dto = new UserRequestDTO("Teste User", "duplicado@email.com", "123456");
        userService.create(dto);

        UserRequestDTO dtoDuplicado = new UserRequestDTO("Outro User", "duplicado@email.com", "123456");

        assertThatThrownBy(() -> userService.create(dtoDuplicado))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Email já cadastrado");
    }

    @Test
    void deveBuscarUsuarioPorId() {
        UserRequestDTO dto = new UserRequestDTO("Teste User", "busca@email.com", "123456");
        UserResponseDTO created = userService.create(dto);
        UserResponseDTO found = userService.buscarPorId(created.id());

        assertThat(found).isNotNull();
        assertThat(found.id()).isEqualTo(created.id());
        assertThat(found.email()).isEqualTo("busca@email.com");
    }

    @Test
    void deveLancarExcecaoAoBuscarUsuarioInexistente() {
        assertThatThrownBy(() -> userService.buscarPorId("id_inexistente"))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Usuário não encontrado");
    }

    @Test
    void deveAtualizarUsuarioComSucesso() {
        UserRequestDTO dto = new UserRequestDTO("Original", "original@email.com", "123456");
        UserResponseDTO created = userService.create(dto);

        UserRequestDTO updateDto = new UserRequestDTO("Atualizado", "atualizado@email.com", "654321");
        UserResponseDTO updated = userService.atualizar(created.id(), updateDto);

        assertThat(updated.nome()).isEqualTo("Atualizado");
        assertThat(updated.email()).isEqualTo("atualizado@email.com");
    }

    @Test
    void deveDeletarUsuarioComSucesso() {
        UserRequestDTO dto = new UserRequestDTO("Deletar", "deletar@email.com", "123456");
        UserResponseDTO created = userService.create(dto);
        userService.deletar(created.id());

        assertThat(userRepository.findById(created.id())).isEmpty();
    }
}