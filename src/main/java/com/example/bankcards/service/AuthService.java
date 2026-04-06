package com.example.bankcards.service;

import com.example.bankcards.dto.auth.AuthRequestDTO;

public interface AuthService {
    String authenticate(AuthRequestDTO request);
}
