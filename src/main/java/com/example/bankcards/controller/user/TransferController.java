package com.example.bankcards.controller.user;

import com.example.bankcards.dto.transfer.TransferDTO;
import com.example.bankcards.security.CustomUserDetails;
import com.example.bankcards.service.TransferService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

    // How user should choose exact card is not specified is technical specification
    @PostMapping
    public ResponseEntity<Void> transferBetweenCards(
            @AuthenticationPrincipal CustomUserDetails user,
            @RequestBody @Valid TransferDTO dto) {
        log.info("Received transfer request from {} to {} amount {}", dto.getFromId(), dto.getToId(), dto.getAmount());
        transferService.processTransfer(user.getId(), dto);
        return ResponseEntity.ok().build();
    }
}
