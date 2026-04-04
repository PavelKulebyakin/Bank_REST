package com.example.bankcards.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO для обновления данных пользователя.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateDTO {

    /**
     * Полное имя пользователя.
     * Формат: "Фамилия Имя".
     */
    private String fullName;

    /**
     * Email пользователя.
     */
    @Email(message = "Email should be valid")
    private String email;

    /**
     * Пароль пользователя.
     */
    private String password;

    /**
     * Роль пользователя.
     * Допустимые значения: "USER", "ADMIN".
     * По умолчанию — "USER".
     */
    private String role;
}
