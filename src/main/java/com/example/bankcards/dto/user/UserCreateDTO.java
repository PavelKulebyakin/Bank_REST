package com.example.bankcards.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * DTO для создания нового пользователя.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserCreateDTO {

    /**
     * Полное имя пользователя.
     * Формат: "Фамилия Имя".
     */
    @NotBlank(message = "Full name is required")
    private String fullName;

    /**
     * Email пользователя.
     */
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    /**
     * Пароль пользователя.
     */
    @NotBlank(message = "Password is required")
    private String password;

    /**
     * Роль пользователя.
     * Допустимые значения: "USER", "ADMIN".
     * По умолчанию — "USER".
     */
    private String role;
}
