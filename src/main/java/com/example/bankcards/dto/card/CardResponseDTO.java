package com.example.bankcards.dto.card;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO для возврата данных карты.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CardResponseDTO {

    /**
     * Уникальный идентификатор карты.
     */
    private Long id;

    /**
     * Последние четыре цифры карты для отображения в формате "**** **** **** 1234"
     */
    private String last4Digits;

    /**
     * Имя владельца карты.
     */
    private String cardholderName;

    /**
     * Срок действия карты в формате "12/30"
     */
    private String expirationDate;

    /**
     * Статус карты
     * Допустимые значения: "ACTIVE", "BLOCKED", "EXPIRED".
     */
    private String status;
}
