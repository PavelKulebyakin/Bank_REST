package com.example.bankcards.dto;

import lombok.Builder;

@Builder
public record AuthResponseDTO(
        String accessToken
) {}
