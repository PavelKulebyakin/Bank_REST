package com.example.bankcards.service;

import com.example.bankcards.dto.card.CardCreateDTO;
import com.example.bankcards.dto.card.CardInfoResponseDTO;
import com.example.bankcards.dto.card.CardStatusUpdateDTO;

import java.util.List;

public interface AdminCardService {
    CardInfoResponseDTO addNewCard(CardCreateDTO dto);

    List<CardInfoResponseDTO> getAllCards();

    List<CardInfoResponseDTO> getAllCardsWithStatusAndPaging(String status, int page, int size);

    CardInfoResponseDTO getCardById(Long id);

    void deleteCardById(Long id);

    CardInfoResponseDTO updateCardStatusById(Long id, CardStatusUpdateDTO dto);
}
