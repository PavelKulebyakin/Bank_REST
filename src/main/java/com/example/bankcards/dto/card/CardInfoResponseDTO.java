package com.example.bankcards.dto.card;

import lombok.*;

/**
 * DTO для возврата данных карты.
 *
 * @param id             Уникальный идентификатор карты.
 * @param maskedNumber   Маска + последние четыре цифры карты для отображения в формате "**** **** **** 1234"
 * @param cardholderName Имя владельца карты.
 * @param expirationDate Срок действия карты в формате "12/30"
 * @param status         Статус карты
 *                       Допустимые значения: "ACTIVE", "BLOCKED", "EXPIRED".
 */
@Builder
public record CardInfoResponseDTO(
        Long id,
        String maskedNumber,
        String cardholderName,
        String expirationDate,
        String status
) {}
