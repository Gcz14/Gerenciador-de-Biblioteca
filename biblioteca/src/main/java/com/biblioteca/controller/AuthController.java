package com.biblioteca.controller;

import com.biblioteca.dto.AuthRequestDTO;
import com.biblioteca.dto.AuthResponseDTO;
import com.biblioteca.dto.UserRequestDTO;
import com.biblioteca.dto.UserResponseDTO;
import com.biblioteca.security.JwtService;
import com.biblioteca.service.CustomUserDetailsService;
import com.biblioteca.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;
    private final UserService userService;

    @PostMapping("/login")
    public AuthResponseDTO login(@RequestBody @Valid AuthRequestDTO authRequest) {
        System.out.println("=== TENTANDO LOGIN ===");
        System.out.println("Email: " + authRequest.getEmail());
        System.out.println("Senha: " + authRequest.getSenha());
        
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getSenha())
            );
            System.out.println("Autenticação bem sucedida!");
            
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            var user = userDetailsService.findByEmail(userDetails.getUsername());
            
            String token = jwtService.generateToken(user.getId(), user.getEmail());
            
            return new AuthResponseDTO(token, user.getId(), user.getEmail());
        } catch (BadCredentialsException e) {
            System.out.println("Credenciais inválidas!");
            throw e;
        } catch (Exception e) {
            System.out.println("Erro na autenticação: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @PostMapping("/register")
    public UserResponseDTO register(@RequestBody @Valid UserRequestDTO userRequestDTO) {
        return userService.create(userRequestDTO);
    }
}