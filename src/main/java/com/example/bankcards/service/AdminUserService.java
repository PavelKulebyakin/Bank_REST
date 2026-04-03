package com.example.bankcards.service;

import com.example.bankcards.dto.user.UserCreateDTO;
import com.example.bankcards.dto.user.UserResponseDTO;
import com.example.bankcards.dto.user.UserUpdateDTO;
import jakarta.validation.constraints.Min;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface AdminUserService {
    UserResponseDTO createUser(UserCreateDTO dto);

    List<UserResponseDTO> getAllUsers();

    UserResponseDTO getUserById(Long id);

    void deleteUserById(Long id);

    UserResponseDTO updateUserById(Long id, UserUpdateDTO dto);

    ResponseEntity<List<UserResponseDTO>> getAllUsersWithRole(String role);

    ResponseEntity<List<UserResponseDTO>> getAllUsersWithPagingAndRole(int page, int size, String role);
}
