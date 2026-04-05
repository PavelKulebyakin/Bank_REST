package com.example.bankcards.dto.user;

import lombok.*;

/**
 * DTO для возврата данных пользователя.
 *
 * @param id       Уникальный идентификатор пользователя.
 * @param fullName Полное имя пользователя в формате "Фамилия Имя".
 * @param email    Email пользователя.
 * @param role     Роль пользователя.
 *                 Допустимые значения: "USER", "ADMIN".
 */
@Builder
public record UserInfoResponseDTO(
        Long id,
        String fullName,
        String email,
        String role
) {}
