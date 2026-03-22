package com.biblioteca.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class LivroResponseDTO {
    private String id;
    private String titulo;
    private String autor;
    private String isbn;
    private Integer anoPublicacao;
    private String categoria;
    private String descricao;
    private String status;
    private String usuarioId;
    private LocalDateTime dataCadastro;
    private LocalDateTime dataAtualizacao;
}