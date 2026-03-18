package com.biblioteca.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserRequestDTO(

    @NotBlank
    String nome,

    @Email
    String email,

    @NotBlank
    String senha
) {}