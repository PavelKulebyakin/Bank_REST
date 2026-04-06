package com.example.bankcards.dto.auth;

import lombok.Builder;

@Builder
public record AuthResponseDTO(
        String accessToken
) {}
