package com.example.bankcards.service.impl;

import com.example.bankcards.dto.card.CardBalanceResponseDTO;
import com.example.bankcards.dto.card.CardInfoResponseDTO;
import com.example.bankcards.entity.CardEntity;
import com.example.bankcards.entity.CardStatus;
import com.example.bankcards.exception.custom.ResourceNotFoundException;
import com.example.bankcards.mapper.CardMapper;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.service.UserCardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
class UserCardServiceImpl implements UserCardService {

    private final CardRepository cardRepository;
    private final CardMapper cardMapper;

    @Override
    public List<CardInfoResponseDTO> getAllCards(Long userId) {
        List<CardEntity> cards = cardRepository.findAllByUserId(userId);
        return cards.stream()
                .map(cardMapper::toDto)
                .toList();
    }

    @Override
    public CardInfoResponseDTO getAllCardsByPanLastDigits(Long userId, String panLast4) {
        CardEntity card = cardRepository.findFirstByUserIdAndLast4Digits(userId, panLast4)
                .orElseThrow(() -> new ResourceNotFoundException("Card wit last 4 digits : " + panLast4 + " not found"));
        return cardMapper.toDto(card);
    }

    // TODO add another way to get specific card
    @Override
    public CardInfoResponseDTO getCardsById(Long userId, Long cardId) {
        CardEntity card = cardRepository.findByIdAndUserId(cardId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Card with id : " + cardId + " not found"));
        return cardMapper.toDto(card);
    }

    @Override
    public CardBalanceResponseDTO getCardBalance(Long userId, Long cardId) {
        CardEntity card = cardRepository.findByIdAndUserId(cardId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Card with id: " + cardId + " not found"));
        return CardBalanceResponseDTO.builder()
                .cardId(card.getId())
                .balance(card.getBalance())
                .build();
    }

    @Override
    @Transactional
    public void blockCard(Long userId, Long cardId) {
        CardEntity card = cardRepository.findByIdAndUserId(cardId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Card with id : " + cardId + " not found"));

        // Можно добавить статус REQUEST_BLOCK если мы не хотим чтобы пользователь мог сам блокировать карту
        if (card.getStatus() == CardStatus.BLOCKED) {
            throw new IllegalStateException("Card is already blocked");
        }
        card.setStatus(CardStatus.BLOCKED);
        cardRepository.save(card);
    }

    @Override
    public List<CardInfoResponseDTO> getAllCardsWithStatusAndPaging(Long userId, String status, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page - 1, size);

        if (status == null || status.isBlank()) {
            return getAllCardsWithPaging(userId, pageable);
        } else {
            return getCardsByStatusAndPaging(userId, status, pageable);
        }
    }

    private List<CardInfoResponseDTO> getAllCardsWithPaging(Long userId, Pageable pageable) {
        Page<CardEntity> cardsPage = cardRepository.findAllByUserId(userId, pageable);
        return cardsPage.stream()
                .map(cardMapper::toDto)
                .toList();
    }

    private List<CardInfoResponseDTO> getCardsByStatusAndPaging(Long userId, String status, Pageable pageable) {
        CardStatus cardStatus;
        try {
            cardStatus = CardStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Invalid status: " + status);
        }

        Page<CardEntity> cardsPage = cardRepository.findAllByUserIdAndStatus(userId, cardStatus, pageable);
        return cardsPage.stream()
                .map(cardMapper::toDto)
                .toList();
    }
}
