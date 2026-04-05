package com.example.bankcards.controller.user;

import com.example.bankcards.dto.card.CardBalanceResponseDTO;
import com.example.bankcards.dto.card.CardInfoResponseDTO;
import com.example.bankcards.security.CustomUserDetails;
import com.example.bankcards.service.UserCardService;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/me/cards")
@RequiredArgsConstructor
@Log4j2
public class UserCardController {

    private final UserCardService userCardService;

    @GetMapping("/all")
    public ResponseEntity<List<CardInfoResponseDTO>> getUserCards(
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        log.info("Resieved request to get all cards");
        List<CardInfoResponseDTO> cards = userCardService.getAllCards(user.getId());
        return ResponseEntity.ok().body(cards);
    }

    @GetMapping
    public ResponseEntity<List<CardInfoResponseDTO>> getUserCardsByStatusWithPagination(
            @AuthenticationPrincipal CustomUserDetails user,
            @RequestParam(required = false) String status,
            @RequestParam @Min(1) int page,
            @RequestParam @Min(1) int size) {
        log.info("Received request to get cards with status {}, page {} and size {}", status, page, size);
        List<CardInfoResponseDTO> cards = userCardService.getAllCardsWithStatusAndPaging(user.getId(), status, page, size);
        return ResponseEntity.ok().body(cards);
    }

    @GetMapping("/pan")
    public ResponseEntity<CardInfoResponseDTO> getUserCardsByPan(
            @AuthenticationPrincipal CustomUserDetails user,
            @RequestParam("last_4") String panLast4) {
        log.info("Received request to get cards with last 4 digits {}", panLast4);
        CardInfoResponseDTO card = userCardService.getAllCardsByPanLastDigits(user.getId(), panLast4);
        return ResponseEntity.ok().body(card);
    }

    @GetMapping("/{cardId}")
    public ResponseEntity<CardInfoResponseDTO> getUserCardsById(
            @AuthenticationPrincipal CustomUserDetails user,
            @PathVariable long cardId) {
        log.info("Received request to get card with ID {}", cardId);
        CardInfoResponseDTO card = userCardService.getCardsById(user.getId(), cardId);
        return ResponseEntity.ok().body(card);
    }

    @GetMapping("/{cardId}/balance")
    public ResponseEntity<CardBalanceResponseDTO> getCardBalance(
            @AuthenticationPrincipal CustomUserDetails user,
            @PathVariable long cardId) {
        log.info("Received request to get balance for card ID {}", cardId);
        CardBalanceResponseDTO balance = userCardService.getCardBalance(user.getId(), cardId);
        return ResponseEntity.ok().body(balance);
    }

    @PostMapping("/{cardId}/block")
    public ResponseEntity<Void> blockCard(
            @AuthenticationPrincipal CustomUserDetails user,
            @PathVariable long cardId) {
        log.info("Received request to block card ID {}", cardId);
        userCardService.blockCard(user.getId(), cardId);
        return ResponseEntity.ok().build();
    }
}
