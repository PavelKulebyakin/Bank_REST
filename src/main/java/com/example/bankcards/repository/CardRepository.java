package com.example.bankcards.repository;

import com.example.bankcards.entity.CardEntity;
import com.example.bankcards.entity.CardStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@Repository
public interface CardRepository extends JpaRepository<CardEntity, Long> {
    boolean existsByCardNumber(String cardNumber);

    List<CardEntity> findAllByUserId(Long userId);

    Optional<CardEntity> findByIdAndUserId(Long cardId, Long userId);

    Page<CardEntity> findAllByUserIdAndStatus(Long userId, CardStatus status, Pageable pageable);

    Optional<CardEntity> findFirstByUserIdAndLast4Digits(Long userId, String panLast4);

    Page<CardEntity> findAllByUserId(Long userId, Pageable pageable);

    Page<CardEntity> findAllByStatus(CardStatus cardStatus, Pageable pageable);
}
