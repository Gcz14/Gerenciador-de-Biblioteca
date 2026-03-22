package com.biblioteca.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Data
@Document(collection = "livros")
public class Livro {
    
    @Id
    private String id;
    
    private String titulo;
    private String autor;
    private String isbn;
    private Integer anoPublicacao;
    private String categoria;
    private String descricao;
    private String status; // DISPONIVEL, EMPRESTADO, LIDO
    private String usuarioId; // ID do dono do livro
    
    private LocalDateTime dataCadastro;
    private LocalDateTime dataAtualizacao;
}