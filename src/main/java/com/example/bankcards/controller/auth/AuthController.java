package com.example.bankcards.controller.auth;

import com.example.bankcards.dto.auth.AuthResponseDTO;
import com.example.bankcards.dto.auth.AuthRequestDTO;
import com.example.bankcards.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Log4j2
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody @Valid AuthRequestDTO request) {
        log.info("Received request to login with email: {}, password: {}", request.getEmail(), request.getPassword());
        String token = authService.authenticate(request);
        return ResponseEntity.ok().body(new AuthResponseDTO(token));
    }
}
