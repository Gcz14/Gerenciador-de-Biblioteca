package com.biblioteca.security;

import com.biblioteca.config.TestcontainersConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class JwtServiceTest {

    @Autowired
    private JwtService jwtService;

    private String usuarioId = "673a1b2c3d4e5f6a7b8c9d0e";
    private String email = "teste@email.com";

    @Test
    void deveGerarTokenComSucesso() {
        String token = jwtService.generateToken(usuarioId, email);
        
        assertThat(token).isNotNull();
        assertThat(token).isNotEmpty();
    }

    @Test
    void deveExtrairEmailDoToken() {
        String token = jwtService.generateToken(usuarioId, email);
        String extractedEmail = jwtService.extractEmail(token);
        
        assertThat(extractedEmail).isEqualTo(email);
    }

    @Test
    void deveExtrairUsuarioIdDoToken() {
        String token = jwtService.generateToken(usuarioId, email);
        String extractedUserId = jwtService.extractUserId(token);
        
        assertThat(extractedUserId).isEqualTo(usuarioId);
    }

    @Test
    void deveValidarTokenComUserDetailsCorreto() {
        String token = jwtService.generateToken(usuarioId, email);
        UserDetails userDetails = User.withUsername(email).password("").authorities(new ArrayList<>()).build();
        
        boolean isValid = jwtService.isTokenValid(token, userDetails);
        
        assertThat(isValid).isTrue();
    }

    @Test
    void deveRejeitarTokenComUserDetailsDiferente() {
        String token = jwtService.generateToken(usuarioId, email);
        UserDetails userDetails = User.withUsername("outro@email.com").password("").authorities(new ArrayList<>()).build();
        
        boolean isValid = jwtService.isTokenValid(token, userDetails);
        
        assertThat(isValid).isFalse();
    }
}