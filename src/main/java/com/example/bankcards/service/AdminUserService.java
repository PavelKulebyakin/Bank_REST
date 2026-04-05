package com.example.bankcards.service;

import com.example.bankcards.dto.user.UserCreateDTO;
import com.example.bankcards.dto.user.UserInfoResponseDTO;
import com.example.bankcards.dto.user.UserUpdateDTO;

import java.util.List;

public interface AdminUserService {
    UserInfoResponseDTO createUser(UserCreateDTO dto);

    List<UserInfoResponseDTO> getAllUsers();

    UserInfoResponseDTO getUserById(Long id);

    void deleteUserById(Long id);

    UserInfoResponseDTO updateUserById(Long id, UserUpdateDTO dto);

    List<UserInfoResponseDTO> getAllUsersWithPagingAndRole(int page, int size, String role);
}
