package com.example.bankcards.service.impl;

import com.example.bankcards.dto.user.UserCreateDTO;
import com.example.bankcards.dto.user.UserResponseDTO;
import com.example.bankcards.dto.user.UserUpdateDTO;
import com.example.bankcards.entity.UserEntity;
import com.example.bankcards.exception.custom.ResourceAlreadyExistsException;
import com.example.bankcards.exception.custom.ResourceNotFoundException;
import com.example.bankcards.mapper.UserMapper;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.service.AdminUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminUserServiceImpl implements AdminUserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public UserResponseDTO createUser(UserCreateDTO dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new ResourceAlreadyExistsException("Email уже используется: "  + dto.getEmail());
        }
        UserEntity user = userMapper.toEntity(dto);
        UserEntity savedUser = userRepository.save(user);
        return userMapper.toDto(savedUser);
    }

    @Override
    public List<UserResponseDTO> getAllUsers() {
        return userRepository
                .findAll()
                .stream()
                .map(userMapper::toDto)
                .toList();
    }

    @Override
    public UserResponseDTO getUserById(Long id) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь с id: " + id + " не найден"));
        return userMapper.toDto(user);
    }

    @Override
    @Transactional
    public void deleteUserById(Long id) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь с id: " + id + " не найден"));
        userRepository.delete(user);
    }

    @Override
    @Transactional
    public UserResponseDTO updateUserById(Long id, UserUpdateDTO dto) {
        UserEntity existingUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь с id: " + id + " не найден"));

        if (dto.getFullName() != null && !dto.getFullName().isBlank()) {
            existingUser.setFullName(dto.getFullName());
        }
        if (dto.getEmail() != null && !dto.getEmail().isBlank()) {
            existingUser.setEmail(dto.getEmail());
        }
        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            existingUser.setPassword(dto.getPassword());
        }
        if (dto.getRole() != null) {
            existingUser.setRole(UserMapper.stringToRole(dto.getRole()));
        }

        UserEntity updatedUser = userRepository.save(existingUser);

        return userMapper.toDto(updatedUser);
    }

    // TODO add filters and pagination support
    @Override
    public List<UserResponseDTO> getAllUsersWithPagingAndRole(int page, int size, String role) {
        return getAllUsers();
    }
}
