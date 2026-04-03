package com.example.bankcards.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO для возврата данных пользователя клиенту.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDTO {

    /**
     * Уникальный идентификатор пользователя.
     */
    private Long id;

    /**
     * Полное имя пользователя в формате "Фамилия Имя".
     */
    private String fullName;

    /**
     * Email пользователя.
     */
    private String email;

    /**
     * Роль пользователя.
     * Допустимые значения: "USER", "ADMIN".
     */
    private String role;
}
