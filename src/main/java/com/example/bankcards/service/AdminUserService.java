package com.example.bankcards.service;

import com.example.bankcards.dto.user.UserCreateDTO;
import com.example.bankcards.dto.user.UserResponseDTO;
import com.example.bankcards.dto.user.UserUpdateDTO;

import java.util.List;

public interface AdminUserService {
    UserResponseDTO createUser(UserCreateDTO dto);

    List<UserResponseDTO> getAllUsers();

    UserResponseDTO getUserById(Long id);

    void deleteUserById(Long id);

    UserResponseDTO updateUserById(Long id, UserUpdateDTO dto);

    List<UserResponseDTO> getAllUsersWithPagingAndRole(int page, int size, String role);
}
