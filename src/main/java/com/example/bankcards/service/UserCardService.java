package com.example.bankcards.service;

import com.example.bankcards.dto.card.CardBalanceResponseDTO;
import com.example.bankcards.dto.card.CardInfoResponseDTO;

import java.util.List;

public interface UserCardService {
    List<CardInfoResponseDTO> getAllCards(Long userId);

    CardInfoResponseDTO getAllCardsByPanLastDigits(Long userId, String panLast4);

    CardInfoResponseDTO getCardsById(Long userId, Long cardId);

    CardBalanceResponseDTO getCardBalance(Long userId, Long cardId);

    void blockCard(Long userId, Long cardId);

    List<CardInfoResponseDTO> getAllCardsWithStatusAndPaging(Long userId, String status, Integer page, Integer size);
}
