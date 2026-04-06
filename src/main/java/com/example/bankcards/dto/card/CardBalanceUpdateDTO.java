package com.example.bankcards.dto.card;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO для обновления баланса банковской карты.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CardBalanceUpdateDTO {

    /**
     * Новый статус карты.
     * Допустимые значения: "ACTIVE", "BLOCKED".
     */
    @NotNull(message = "Balance is required")
    private BigDecimal balance;
}
