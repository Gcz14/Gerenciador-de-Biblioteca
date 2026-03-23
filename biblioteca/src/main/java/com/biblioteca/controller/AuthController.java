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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;
    private final UserService userService;

    @PostMapping("/login")
    public AuthResponseDTO login(@RequestBody @Valid AuthRequestDTO authRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getSenha())
        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        var user = userDetailsService.findByEmail(userDetails.getUsername());
        
        String token = jwtService.generateToken(user.getId(), user.getEmail());
        
        return new AuthResponseDTO(token, user.getId(), user.getEmail());
    }

    @PostMapping("/register")
    public UserResponseDTO register(@RequestBody @Valid UserRequestDTO userRequestDTO) {
        return userService.create(userRequestDTO);
    }
}