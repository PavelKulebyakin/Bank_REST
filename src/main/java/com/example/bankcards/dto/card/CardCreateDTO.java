package com.example.bankcards.dto.card;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO для создания новой банковской карты администратором.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CardCreateDTO {

    /**
     * Имя владельца карты в формате "ФАМИЛИЯ ИМЯ".
     */
    @NotBlank(message = "Cardholder name is required")
    private String cardholderName;

    /**
     * Id пользователя.
     */
    @NotNull(message = "User ID is required")
    private Long userId;

    /**
     * Срок действия карты в годах.
     * По умолчанию 4 года.
     */
    private Integer validityPeriod;
}
