package com.biblioteca.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponseDTO {
    private String token;
    private String type = "Bearer";
    private String userId;
    private String email;
    
    public AuthResponseDTO(String token, String userId, String email) {
        this.token = token;
        this.userId = userId;
        this.email = email;
    }
}