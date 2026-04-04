package com.example.bankcards.dto.card;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CardBalanceDTO {

    /**
     * Id карты
     */
    private Long cardId;

    /**
     * Баланс карты
     */
    private BigDecimal balance;

    /**
     * Валюта баланса
     */
    private String currency;
}
