package com.biblioteca.controller;

import com.biblioteca.dto.LivroRequestDTO;
import com.biblioteca.dto.LivroResponseDTO;
import com.biblioteca.security.JwtService;
import com.biblioteca.service.LivroService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/livros")
@RequiredArgsConstructor
public class LivroController {

    private final LivroService livroService;
    private final JwtService jwtService;

    private String getUsuarioIdFromToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        String token = authHeader.substring(7);
        return jwtService.extractUserId(token);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LivroResponseDTO criarLivro(@RequestBody @Valid LivroRequestDTO dto, HttpServletRequest request) {
        String usuarioId = getUsuarioIdFromToken(request);
        return livroService.criarLivro(dto, usuarioId);
    }

    @GetMapping
    public List<LivroResponseDTO> listarLivros(HttpServletRequest request) {
        String usuarioId = getUsuarioIdFromToken(request);
        return livroService.listarLivrosPorUsuario(usuarioId);
    }

    @GetMapping("/{id}")
    public LivroResponseDTO buscarPorId(@PathVariable String id, HttpServletRequest request) {
        String usuarioId = getUsuarioIdFromToken(request);
        return livroService.buscarPorId(id, usuarioId);
    }

    @PutMapping("/{id}")
    public LivroResponseDTO atualizarLivro(@PathVariable String id, @RequestBody @Valid LivroRequestDTO dto, HttpServletRequest request) {
        String usuarioId = getUsuarioIdFromToken(request);
        return livroService.atualizarLivro(id, dto, usuarioId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletarLivro(@PathVariable String id, HttpServletRequest request) {
        String usuarioId = getUsuarioIdFromToken(request);
        livroService.deletarLivro(id, usuarioId);
    }
}