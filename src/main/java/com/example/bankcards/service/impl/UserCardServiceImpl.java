package com.example.bankcards.service.impl;

import com.example.bankcards.dto.card.CardBalanceDTO;
import com.example.bankcards.dto.card.CardResponseDTO;
import com.example.bankcards.service.UserCardService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
class UserCardServiceImpl implements UserCardService {

    @Override
    public List<CardResponseDTO> getAllCards() {
        return List.of();
    }

    @Override
    public List<CardResponseDTO> getAllCardsWithPaging(Integer page, Integer size) {
        return List.of();
    }

    @Override
    public List<CardResponseDTO> getAllCardsWithStatus(String status) {
        return List.of();
    }

    @Override
    public List<CardResponseDTO> getAllCardsByPanLastDigits(String panLast4) {
        return List.of();
    }

    @Override
    public List<CardResponseDTO> getAllCardsById(Long id) {
        return List.of();
    }

    @Override
    public CardBalanceDTO getCardBalance(Long id) {
        return null;
    }

    @Override
    public void blockCard(Long id) {}

    @Override
    public List<CardResponseDTO> getAllCardsWithStatusAndPaging(String status, Integer page, Integer size) {
        return List.of();
    }
}
