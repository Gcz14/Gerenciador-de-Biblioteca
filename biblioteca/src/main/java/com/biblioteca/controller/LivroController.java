package com.biblioteca.controller;

import com.biblioteca.dto.LivroRequestDTO;
import com.biblioteca.dto.LivroResponseDTO;
import com.biblioteca.service.LivroService;
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
    
    // Por enquanto vamos usar um ID fixo para teste
    // Depois vamos pegar do token JWT
    private static final String USUARIO_TESTE_ID = "69bf2db636151283511d3cf2"; // ID do seu usuário de teste

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LivroResponseDTO criarLivro(@RequestBody @Valid LivroRequestDTO dto) {
        return livroService.criarLivro(dto, USUARIO_TESTE_ID);
    }

    @GetMapping
    public List<LivroResponseDTO> listarLivros() {
        return livroService.listarLivrosPorUsuario(USUARIO_TESTE_ID);
    }

    @GetMapping("/{id}")
    public LivroResponseDTO buscarPorId(@PathVariable String id) {
        return livroService.buscarPorId(id, USUARIO_TESTE_ID);
    }

    @PutMapping("/{id}")
    public LivroResponseDTO atualizarLivro(@PathVariable String id, @RequestBody @Valid LivroRequestDTO dto) {
        return livroService.atualizarLivro(id, dto, USUARIO_TESTE_ID);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletarLivro(@PathVariable String id) {
        livroService.deletarLivro(id, USUARIO_TESTE_ID);
    }
}