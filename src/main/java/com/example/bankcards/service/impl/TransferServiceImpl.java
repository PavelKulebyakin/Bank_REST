package com.example.bankcards.service.impl;

import com.example.bankcards.dto.transfer.TransferDTO;
import com.example.bankcards.entity.CardEntity;
import com.example.bankcards.entity.TransferEntity;
import com.example.bankcards.exception.custom.ResourceNotFoundException;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.TransferRepository;
import com.example.bankcards.service.TransferService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
class TransferServiceImpl implements TransferService {

    private final TransferRepository transferRepository;
    private final CardRepository cardRepository;

    @Override
    @Transactional
    public void processTransfer(Long id, TransferDTO dto) {
        CardEntity fromCard = cardRepository.findByIdAndUserId(dto.getFromId(), id)
                .orElseThrow(() -> new ResourceNotFoundException("Карта отправителя не найдена"));

        CardEntity toCard = cardRepository.findById(dto.getToId())
                .orElseThrow(() -> new ResourceNotFoundException("Карта получателя не найдена"));

        if (fromCard.getBalance().compareTo(dto.getAmount()) < 0) {
            throw new IllegalStateException("Недостаточно средств на карте");
        }

        fromCard.setBalance(fromCard.getBalance().subtract(dto.getAmount()));
        toCard.setBalance(toCard.getBalance().add(dto.getAmount()));

        cardRepository.save(fromCard);
        cardRepository.save(toCard);

        TransferEntity transfer = TransferEntity.builder()
                .fromCard(fromCard)
                .toCard(toCard)
                .amount(dto.getAmount())
                .build();

        transferRepository.save(transfer);
    }
}
