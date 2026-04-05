package com.example.bankcards.dto.card;

import lombok.*;

import java.math.BigDecimal;

/**
 * @param cardId  Id карты
 * @param balance Баланс карты в формате "1000.00"
 */
@Builder
public record CardBalanceResponseDTO(
        Long cardId,
        BigDecimal balance
) {}
