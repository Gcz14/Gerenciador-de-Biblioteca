package com.biblioteca.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LivroRequestDTO {
    
    @NotBlank(message = "Título é obrigatório")
    @Size(min = 1, max = 200, message = "Título deve ter entre 1 e 200 caracteres")
    private String titulo;
    
    @NotBlank(message = "Autor é obrigatório")
    @Size(min = 1, max = 100, message = "Autor deve ter entre 1 e 100 caracteres")
    private String autor;
    
    @Size(min = 10, max = 13, message = "ISBN deve ter entre 10 e 13 caracteres")
    private String isbn;
    
    private Integer anoPublicacao;
    
    private String categoria;
    private String descricao;
    
    @NotBlank(message = "Status é obrigatório")
    private String status;
}   