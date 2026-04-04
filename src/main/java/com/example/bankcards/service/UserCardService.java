package com.example.bankcards.service;

import com.example.bankcards.dto.card.CardBalanceDTO;
import com.example.bankcards.dto.card.CardResponseDTO;

import java.util.List;

public interface UserCardService {
    List<CardResponseDTO> getAllCards();

    List<CardResponseDTO> getAllCardsWithPaging(Integer page, Integer size);

    List<CardResponseDTO> getAllCardsWithStatus(String status);

    List<CardResponseDTO> getAllCardsByPanLastDigits(String panLast4);

    List<CardResponseDTO> getAllCardsById(Long id);

    CardBalanceDTO getCardBalance(Long id);

    void blockCard(Long id);

    List<CardResponseDTO> getAllCardsWithStatusAndPaging(String status, Integer page, Integer size);
}
