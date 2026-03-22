package com.biblioteca.service;

import com.biblioteca.dto.LivroRequestDTO;
import com.biblioteca.dto.LivroResponseDTO;
import com.biblioteca.exception.ResourceNotFoundException;
import com.biblioteca.model.Livro;
import com.biblioteca.repository.LivroRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LivroService {

    private final LivroRepository livroRepository;

    public LivroResponseDTO criarLivro(LivroRequestDTO dto, String usuarioId) {
        Livro livro = new Livro();
        livro.setTitulo(dto.getTitulo());
        livro.setAutor(dto.getAutor());
        livro.setIsbn(dto.getIsbn());
        livro.setAnoPublicacao(dto.getAnoPublicacao());
        livro.setCategoria(dto.getCategoria());
        livro.setDescricao(dto.getDescricao());
        livro.setStatus(dto.getStatus());
        livro.setUsuarioId(usuarioId);
        livro.setDataCadastro(LocalDateTime.now());
        livro.setDataAtualizacao(LocalDateTime.now());

        Livro saved = livroRepository.save(livro);
        return converterParaDTO(saved);
    }

    public List<LivroResponseDTO> listarLivrosPorUsuario(String usuarioId) {
        return livroRepository.findByUsuarioId(usuarioId)
                .stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }

    public LivroResponseDTO buscarPorId(String id, String usuarioId) {
        Livro livro = livroRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Livro não encontrado com ID: " + id));
        
        // Verificar se o livro pertence ao usuário
        if (!livro.getUsuarioId().equals(usuarioId)) {
            throw new RuntimeException("Livro não pertence ao usuário");
        }
        
        return converterParaDTO(livro);
    }

    public LivroResponseDTO atualizarLivro(String id, LivroRequestDTO dto, String usuarioId) {
        Livro livro = livroRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Livro não encontrado com ID: " + id));
        
        if (!livro.getUsuarioId().equals(usuarioId)) {
            throw new RuntimeException("Livro não pertence ao usuário");
        }
        
        livro.setTitulo(dto.getTitulo());
        livro.setAutor(dto.getAutor());
        livro.setIsbn(dto.getIsbn());
        livro.setAnoPublicacao(dto.getAnoPublicacao());
        livro.setCategoria(dto.getCategoria());
        livro.setDescricao(dto.getDescricao());
        livro.setStatus(dto.getStatus());
        livro.setDataAtualizacao(LocalDateTime.now());
        
        Livro updated = livroRepository.save(livro);
        return converterParaDTO(updated);
    }

    public void deletarLivro(String id, String usuarioId) {
        Livro livro = livroRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Livro não encontrado com ID: " + id));
        
        if (!livro.getUsuarioId().equals(usuarioId)) {
            throw new RuntimeException("Livro não pertence ao usuário");
        }
        
        livroRepository.delete(livro);
    }

    private LivroResponseDTO converterParaDTO(Livro livro) {
        return new LivroResponseDTO(
                livro.getId(),
                livro.getTitulo(),
                livro.getAutor(),
                livro.getIsbn(),
                livro.getAnoPublicacao(),
                livro.getCategoria(),
                livro.getDescricao(),
                livro.getStatus(),
                livro.getUsuarioId(),
                livro.getDataCadastro(),
                livro.getDataAtualizacao()
        );
    }
}