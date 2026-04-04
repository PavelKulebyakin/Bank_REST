package com.example.bankcards.service;

import com.example.bankcards.dto.card.CardCreateDTO;
import com.example.bankcards.dto.card.CardResponseDTO;
import com.example.bankcards.dto.card.CardStatusUpdateDTO;

import java.util.List;

public interface AdminCardService {
    CardResponseDTO addNewCard(CardCreateDTO dto);

    List<CardResponseDTO> getAllCards();

    List<CardResponseDTO> getAllCardsWithStatusAndPaging(String status, int page, int size);

    CardResponseDTO getCardById(Long id);

    void deleteCardById(Long id);

    CardResponseDTO updateCardStatusById(Long id, CardStatusUpdateDTO dto);
}
