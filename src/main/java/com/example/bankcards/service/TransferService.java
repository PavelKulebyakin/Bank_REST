package com.example.bankcards.service;

import com.example.bankcards.dto.transfer.TransferDTO;

public interface TransferService {
    void processTransfer(Long userId, TransferDTO dto);
}
