package com.biblioteca.service;

import com.biblioteca.dto.UserRequestDTO;
import com.biblioteca.dto.UserResponseDTO;
import com.biblioteca.model.User;
import com.biblioteca.repository.UserRepository;
import com.biblioteca.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

        private final UserRepository repository;

        public UserResponseDTO create(UserRequestDTO dto) {
                // regra: email único
                repository.findByEmail(dto.email())
                                .ifPresent(user -> {
                                        throw new IllegalArgumentException("Email já cadastrado");
                                });

                User user = new User();
                user.setNome(dto.nome());
                user.setEmail(dto.email());
                user.setSenha(dto.senha());

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

        // NOVO MÉTODO: Atualizar usuário
        public UserResponseDTO atualizar(String id, UserRequestDTO dto) {
                // Buscar usuário existente
                User usuarioExistente = repository.findById(id)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Usuário não encontrado com ID: " + id));

                // Verificar se o novo email já está em uso por OUTRO usuário
                if (!usuarioExistente.getEmail().equals(dto.email())) {
                        repository.findByEmail(dto.email())
                                        .ifPresent(user -> {
                                                throw new IllegalArgumentException(
                                                                "Email já cadastrado por outro usuário");
                                        });
                }

                // Atualizar dados
                usuarioExistente.setNome(dto.nome());
                usuarioExistente.setEmail(dto.email());
                usuarioExistente.setSenha(dto.senha());

                User usuarioAtualizado = repository.save(usuarioExistente);

                return new UserResponseDTO(
                                usuarioAtualizado.getId(),
                                usuarioAtualizado.getNome(),
                                usuarioAtualizado.getEmail());
        }

        public void deletar(String id) {
                // Verificar se usuário existe
                User usuario = repository.findById(id)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Usuário não encontrado com ID: " + id));

                // Deletar usuário
                repository.delete(usuario);
        }
}