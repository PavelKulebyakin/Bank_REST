package com.example.bankcards.mapper;

import com.example.bankcards.dto.user.UserCreateDTO;
import com.example.bankcards.dto.user.UserInfoResponseDTO;
import com.example.bankcards.entity.Role;
import com.example.bankcards.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public interface UserMapper {

    @Mapping(target = "role", source = "role", qualifiedByName = "stringToRole")
    UserEntity toEntity(UserCreateDTO dto);

    @Mapping(target = "role", source = "role", qualifiedByName = "roleToString")
    UserInfoResponseDTO toDto(UserEntity entity);

    @Named("stringToRole")
    static Role stringToRole(String role) {
        if (role == null || role.isBlank()) return Role.USER;
        try {
            return Role.valueOf(role.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid role: " + role);
        }
    }

    @Named("roleToString")
    static String roleToString(Role role) {
        return role != null ? role.name() : null;
    }
}
