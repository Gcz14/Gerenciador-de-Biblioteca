package com.biblioteca.service;

import com.biblioteca.dto.UserRequestDTO;
import com.biblioteca.dto.UserResponseDTO;
import com.biblioteca.model.User;
import com.biblioteca.repository.UserRepository;
import com.biblioteca.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
        
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    public UserResponseDTO create(UserRequestDTO dto) {
        // regra: email único
        repository.findByEmail(dto.email())
                .ifPresent(user -> {
                    throw new IllegalArgumentException("Email já cadastrado");
                });

        User user = new User();
        user.setNome(dto.nome());
        user.setEmail(dto.email());
        user.setSenha(passwordEncoder.encode(dto.senha())); // CRIPTOGRAFADO

        User saved = repository.save(user);

        return new UserResponseDTO(
                saved.getId(),
                saved.getNome(),
                saved.getEmail());
    }

    public List<UserResponseDTO> listarUsuarios() {
        return repository.findAll()
                .stream()
                .map(user -> new UserResponseDTO(
                        user.getId(),
                        user.getNome(),
                        user.getEmail()))
                .toList();
    }

    public UserResponseDTO buscarPorId(String id) {
        User user = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Usuário não encontrado com ID: " + id));

        return new UserResponseDTO(
                user.getId(),
                user.getNome(),
                user.getEmail());
    }

    public UserResponseDTO atualizar(String id, UserRequestDTO dto) {
        User usuarioExistente = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Usuário não encontrado com ID: " + id));

        if (!usuarioExistente.getEmail().equals(dto.email())) {
            repository.findByEmail(dto.email())
                    .ifPresent(user -> {
                        throw new IllegalArgumentException(
                                "Email já cadastrado por outro usuário");
                    });
        }

        usuarioExistente.setNome(dto.nome());
        usuarioExistente.setEmail(dto.email());
        usuarioExistente.setSenha(passwordEncoder.encode(dto.senha())); // CRIPTOGRAFADO

        User usuarioAtualizado = repository.save(usuarioExistente);

        return new UserResponseDTO(
                usuarioAtualizado.getId(),
                usuarioAtualizado.getNome(),
                usuarioAtualizado.getEmail());
    }

    public void deletar(String id) {
        User usuario = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Usuário não encontrado com ID: " + id));

        repository.delete(usuario);
    }
}