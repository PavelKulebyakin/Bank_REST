package com.example.bankcards.repository;

import com.example.bankcards.entity.Role;
import com.example.bankcards.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    boolean existsByEmail(String email);

    Optional<UserEntity> findByEmail(String email);

    Page<UserEntity> findAllByRole(Role userRole, Pageable pageable);
}
