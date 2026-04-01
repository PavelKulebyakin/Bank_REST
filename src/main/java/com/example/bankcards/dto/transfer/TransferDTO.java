package com.example.bankcards.dto.transfer;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO для перевода средств между картами пользователя.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransferDTO {

    @NotNull(message = "From card ID is required")
    private Long fromId;

    @NotNull(message = "To card ID is required")
    private Long toId;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    private BigDecimal amount;
}
