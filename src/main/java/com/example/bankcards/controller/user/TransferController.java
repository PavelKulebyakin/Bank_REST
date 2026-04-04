package com.example.bankcards.controller.user;

import com.example.bankcards.dto.transfer.TransferDTO;
import com.example.bankcards.service.TransferService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/me/transfer")
@RequiredArgsConstructor
@Log4j2
public class TransferController {

    private final TransferService transferService;

    @PostMapping
    public ResponseEntity<Void> transferBetweenCards(@RequestBody TransferDTO dto) {
        log.info("Received transfer request from {} to {} amount {}", dto.getFromId(), dto.getToId(), dto.getAmount());
        transferService.processTransfer(dto);
        return ResponseEntity.ok().build();
    }
}
