package com.example.bankcards.dto.card;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO для создания новой банковской карты.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CardCreateDTO {

    /**
     * Id пользователя.
     */
    @NotNull(message = "User ID is required")
    private Long userId;

    /**
     * Имя владельца карты в формате "Фамилия Имя".
     */
    @NotBlank(message = "Cardholder name is required")
    private String cardholderName;

    /**
     * Срок действия карты в годах.
     * По умолчанию 4 года.
     */
    private Integer validityPeriod;
}
