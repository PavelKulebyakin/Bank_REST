package com.example.bankcards.service.impl;

import com.example.bankcards.dto.user.UserCreateDTO;
import com.example.bankcards.dto.user.UserInfoResponseDTO;
import com.example.bankcards.dto.user.UserUpdateDTO;
import com.example.bankcards.entity.Role;
import com.example.bankcards.entity.UserEntity;
import com.example.bankcards.exception.custom.InvalidRoleException;
import com.example.bankcards.exception.custom.ResourceAlreadyExistsException;
import com.example.bankcards.exception.custom.ResourceNotFoundException;
import com.example.bankcards.mapper.UserMapper;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.service.AdminUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminUserServiceImpl implements AdminUserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UserInfoResponseDTO createUser(UserCreateDTO dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new ResourceAlreadyExistsException("Email: " + dto.getEmail() + " already exists");
        }
        UserEntity user = userMapper.toEntity(dto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        UserEntity savedUser = userRepository.save(user);
        return userMapper.toDto(savedUser);
    }

    @Override
    public List<UserInfoResponseDTO> getAllUsers() {
        return userRepository
                .findAll()
                .stream()
                .map(userMapper::toDto)
                .toList();
    }

    @Override
    public UserInfoResponseDTO getUserById(Long id) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User with id: " + id + " is not found"));
        return userMapper.toDto(user);
    }

    @Override
    @Transactional
    public void deleteUserById(Long id) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User with id: " + id + " is not found"));
        userRepository.delete(user);
    }

    @Override
    @Transactional
    public UserInfoResponseDTO updateUserById(Long id, UserUpdateDTO dto) {
        UserEntity existingUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User with id: " + id + " is not found"));

        if (dto.getFullName() != null && !dto.getFullName().isBlank()) {
            existingUser.setFullName(dto.getFullName());
        }
        if (dto.getEmail() != null && !dto.getEmail().isBlank()) {
            existingUser.setEmail(dto.getEmail());
        }
        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            existingUser.setPassword(passwordEncoder.encode(dto.getPassword()));
        }
        if (dto.getRole() != null) {
            existingUser.setRole(UserMapper.stringToRole(dto.getRole()));
        }
        UserEntity updatedUser = userRepository.save(existingUser);
        return userMapper.toDto(updatedUser);
    }

    @Override
    public List<UserInfoResponseDTO> getAllUsersWithPagingAndRole(int page, int size, String role) {
        Pageable pageable = PageRequest.of(page - 1, size);

        if (role == null || role.isBlank()) {
            return getAllUsersWithPaging(pageable);
        } else {
            return getUsersByRoleAndPaging(role, pageable);
        }
    }

    private List<UserInfoResponseDTO> getAllUsersWithPaging(Pageable pageable) {
        Page<UserEntity> usersPage = userRepository.findAll(pageable);
        return usersPage.stream()
                .map(userMapper::toDto)
                .toList();
    }

    private List<UserInfoResponseDTO> getUsersByRoleAndPaging(String role, Pageable pageable) {
        Role userRole;
        try {
            userRole = UserMapper.stringToRole(role);
        } catch (InvalidRoleException ex) {
            throw new IllegalArgumentException("Неверная роль пользователя: " + role);
            throw new IllegalArgumentException("Invalid role: " + role);
        }

        Page<UserEntity> usersPage = userRepository.findAllByRole(userRole, pageable);
        return usersPage.stream()
                .map(userMapper::toDto)
                .toList();
    }
}
