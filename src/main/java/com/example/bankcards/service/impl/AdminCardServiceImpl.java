package com.example.bankcards.service.impl;

import com.example.bankcards.dto.card.CardBalanceUpdateDTO;
import com.example.bankcards.dto.card.CardCreateDTO;
import com.example.bankcards.dto.card.CardInfoResponseDTO;
import com.example.bankcards.dto.card.CardStatusUpdateDTO;
import com.example.bankcards.entity.CardEntity;
import com.example.bankcards.entity.CardStatus;
import com.example.bankcards.entity.UserEntity;
import com.example.bankcards.exception.custom.ResourceNotFoundException;
import com.example.bankcards.mapper.CardMapper;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.service.AdminCardService;
import com.example.bankcards.util.EncryptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AdminCardServiceImpl implements AdminCardService {

    private static final int DEFAULT_VALIDITY_YEARS = 4;

    private final CardRepository cardRepository;
    private final UserRepository userRepository;
    private final CardMapper cardMapper;
    private final EncryptionService encryptionService;


    @Override
    @Transactional
    public CardInfoResponseDTO addNewCard(CardCreateDTO dto) {
        UserEntity user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User with id: " + dto.getUserId() + " not found"));

        String plainCardNumber = generateCardNumber();
        String encryptedCardNumber;
        try {
            encryptedCardNumber = encryptionService.encrypt(plainCardNumber);
        } catch (Exception e) {
            throw new RuntimeException("Failed to encrypt card number", e);
        }

        System.out.println("PLAIN: " + plainCardNumber);
        System.out.println("LAST4: " + plainCardNumber.substring(plainCardNumber.length() - 4));

        CardEntity card = CardEntity.builder()
                .user(user)
                .cardNumber(encryptedCardNumber)
                .last4Digits(plainCardNumber.substring(12))
                .cardholderName(dto.getCardholderName())
                .expirationDate(calculateExpirationDate(dto.getValidityPeriod()))
                .status(CardStatus.ACTIVE)
                .balance(BigDecimal.ZERO)
                .build();

        CardEntity savedCard = cardRepository.save(card);
        return cardMapper.toDto(savedCard);
    }

    @Override
    public List<CardInfoResponseDTO> getAllCards() {
        return cardRepository
                .findAll()
                .stream()
                .map(cardMapper::toDto)
                .toList();
    }

    @Override
    public List<CardInfoResponseDTO> getAllCardsWithStatusAndPaging(String status, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        if (status == null || status.isBlank()) {
            return getAllCardsWithPaging(pageable);
        } else {
            return getCardsByStatusAndPaging(status, pageable);
        }
    }

    private List<CardInfoResponseDTO> getAllCardsWithPaging(Pageable pageable) {
        Page<CardEntity> cardsPage = cardRepository.findAll(pageable);
        return cardsPage.stream()
                .map(cardMapper::toDto)
                .toList();
    }

    private List<CardInfoResponseDTO> getCardsByStatusAndPaging(String status, Pageable pageable) {
        CardStatus cardStatus;
        try {
            cardStatus = CardStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Invalid status: " + status);
        }
        Page<CardEntity> cardsPage = cardRepository.findAllByStatus(cardStatus, pageable);
        return cardsPage.stream()
                .map(cardMapper::toDto)
                .toList();
    }

    @Override
    public CardInfoResponseDTO getCardById(Long id) {
        CardEntity card = cardRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Card id " + id + " not found"));
        return cardMapper.toDto(card);
    }

    @Override
    @Transactional
    public void deleteCardById(Long id) {
        CardEntity card = cardRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Card with " + id + " not found"));
        cardRepository.delete(card);
    }

    @Override
    @Transactional
    public CardInfoResponseDTO updateCardStatusById(Long id, CardStatusUpdateDTO dto) {
        CardEntity card = cardRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Card with " + id + " not found"));

        card.setStatus(CardStatus.valueOf(dto.getStatus()));

        CardEntity savedCard = cardRepository.save(card);
        return cardMapper.toDto(savedCard);
    }

    // Card balance update is not specified in specification
    @Override
    public CardInfoResponseDTO updateCardBalance(long id, CardBalanceUpdateDTO dto) {
        CardEntity card = cardRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Card with " + id + " not found"));

        card.setBalance(dto.getBalance());

        CardEntity savedCard = cardRepository.save(card);
        return cardMapper.toDto(savedCard);
    }

    // Card number is not unique because it is encrypted
    @Transactional
    protected String generateCardNumber() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 16; i++) {
            sb.append(random.nextInt(10));
        }
        return  sb.toString();
    }

    private LocalDateTime calculateExpirationDate(Integer validityPeriod) {
        int years = (validityPeriod == null) ? DEFAULT_VALIDITY_YEARS : validityPeriod;
        return LocalDateTime.now().plusYears(years);
    }
}
