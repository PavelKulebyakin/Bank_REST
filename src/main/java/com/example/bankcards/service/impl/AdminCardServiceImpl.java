package com.example.bankcards.service.impl;

import com.example.bankcards.dto.card.CardCreateDTO;
import com.example.bankcards.dto.card.CardResponseDTO;
import com.example.bankcards.dto.card.CardStatusUpdateDTO;
import com.example.bankcards.entity.CardEntity;
import com.example.bankcards.entity.CardStatus;
import com.example.bankcards.entity.UserEntity;
import com.example.bankcards.exception.custom.ResourceNotFoundException;
import com.example.bankcards.mapper.CardMapper;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.service.AdminCardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AdminCardServiceImpl implements AdminCardService {

    private final CardRepository cardRepository;
    private final UserRepository userRepository;
    private final CardMapper cardMapper;

    @Override
    @Transactional
    public CardResponseDTO addNewCard(CardCreateDTO dto) {
        UserEntity user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь с id: " + dto.getUserId() + " не найден"));

        CardEntity cardEntity = CardEntity.builder()
                .user(user)
                .cardNumber(generateUniqueCardNumber())
                .cardholderName(dto.getCardholderName())
                .expirationDate(calculateExpirationDate(dto.getValidityPeriod()))
                .status(CardStatus.ACTIVE)
                .balance(BigDecimal.ZERO)
                .build();

        CardEntity savedCard = cardRepository.save(cardEntity);
        return cardMapper.toDto(savedCard);
    }

    @Override
    public List<CardResponseDTO> getAllCards() {
        return cardRepository
                .findAll()
                .stream()
                .map(cardMapper::toDto)
                .toList();
    }

    // TODO add filters and pagination
    @Override
    public List<CardResponseDTO> getAllCardsWithStatusAndPaging(String status, int page, int size) {
        return getAllCards();
    }

    @Override
    public CardResponseDTO getCardById(Long id) {
        CardEntity card = cardRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Карта с id " + id + " не найдена"));
        return cardMapper.toDto(card);
    }

    @Override
    @Transactional
    public void deleteCardById(Long id) {
        CardEntity card = cardRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Карта с id " + id + " не найдена"));
        cardRepository.delete(card);
    }

    @Override
    @Transactional
    public CardResponseDTO updateCardStatusById(Long id, CardStatusUpdateDTO dto) {
        CardEntity card = cardRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Карта с id " + id + " не найдена"));

        card.setStatus(CardStatus.valueOf(dto.getStatus()));

        CardEntity savedCard = cardRepository.save(card);
        return cardMapper.toDto(savedCard);
    }

    @Transactional
    protected String generateUniqueCardNumber() {
        String cardNumber;
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        do {
            for (int i = 0; i < 16; i++) {
                sb.append(random.nextInt(10));
            }
            cardNumber = sb.toString();
            sb.setLength(0);
        } while (cardRepository.existsByCardNumber(cardNumber));
        return cardNumber;
    }

    private LocalDateTime calculateExpirationDate(Integer validityPeriod) {
        return LocalDateTime.now().plusYears(validityPeriod);
    }
}
