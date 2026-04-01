package com.example.bankcards.dto.card;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO для обновления статуса банковской карты.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CardStatusUpdateDTO {

    /**
     * Новый статус карты.
     * Допустимые значения: "ACTIVE", "BLOCKED".
     */
    @NotBlank(message = "Status is required")
    private String status;
}
