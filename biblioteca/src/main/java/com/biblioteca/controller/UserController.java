package com.biblioteca.controller;

import com.biblioteca.dto.UserRequestDTO;
import com.biblioteca.dto.UserResponseDTO;
import com.biblioteca.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDTO create(@RequestBody @Valid UserRequestDTO dto) {
        return service.create(dto);
    }

    @GetMapping
    public List<UserResponseDTO> listarUsuarios() {
        return service.listarUsuarios();
    }

    @GetMapping("/{id}")
    public UserResponseDTO buscarPorId(@PathVariable String id) {
        return service.buscarPorId(id);
    }

    @PutMapping("/{id}")
    public UserResponseDTO atualizar(@PathVariable String id, @RequestBody @Valid UserRequestDTO dto) {
        return service.atualizar(id, dto);
    }

    // NOVO ENDPOINT: DELETE
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable String id) {
        service.deletar(id);
    }
}