package com.example.bankcards.controller.user;

import com.example.bankcards.dto.card.CardBalanceDTO;
import com.example.bankcards.dto.card.CardResponseDTO;
import com.example.bankcards.dto.transfer.TransferDTO;
import com.example.bankcards.service.UserCardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/me/cards")
@RequiredArgsConstructor
@Log4j2
public class UserCardController {

    private final UserCardService userCardService;

    @GetMapping
    public ResponseEntity<List<CardResponseDTO>> getUserCards() {
        log.info("Resieved request to get all cards");
        List<CardResponseDTO> cards = userCardService.getAllCards();
        return ResponseEntity.ok().body(cards);
    }

    @GetMapping(params = {"page","size"})
    public ResponseEntity<List<CardResponseDTO>> getUserCardsWithPaging(
            @RequestParam Integer page,
            @RequestParam Integer size) {
        log.info("Resieved request to get all cards with page {} and size {}", page, size);
        List<CardResponseDTO> cards = userCardService.getAllCardsWithPaging(page, size);
        return ResponseEntity.ok().body(cards);
    }

    @GetMapping(params = "status")
    public ResponseEntity<List<CardResponseDTO>> getUserCardsByStatus(@RequestParam String status) {
        log.info("Received request to get cards with status {}", status);
        List<CardResponseDTO> cards = userCardService.getAllCardsWithStatus(status);
        return ResponseEntity.ok().body(cards);
    }

    @GetMapping(params = {"status","page","size"})
    public ResponseEntity<List<CardResponseDTO>> getUserCardsByStatusAndPage(
            @RequestParam String status,
            @RequestParam Integer page,
            @RequestParam Integer size) {
        log.info("Received request to get cards with status {}, page {} and size {}", status, page, size);
        List<CardResponseDTO> cards = userCardService.getAllCardsWithStatusAndPaging(status, page, size);
        return ResponseEntity.ok().body(cards);
    }

    @GetMapping(params = "pan_last_4")
    public ResponseEntity<List<CardResponseDTO>> getUserCardsByPan(@RequestParam("pan_last_4") String panLast4) {
        log.info("Received request to get cards with last 4 digits {}", panLast4);
        List<CardResponseDTO> cards = userCardService.getAllCardsByPanLastDigits(panLast4);
        return ResponseEntity.ok().body(cards);
    }

    @GetMapping(params = "id")
    public ResponseEntity<List<CardResponseDTO>> getUserCardsById(@RequestParam Long id) {
        log.info("Received request to get card with ID {}", id);
        List<CardResponseDTO> cards = userCardService.getAllCardsById(id);
        return ResponseEntity.ok().body(cards);
    }

    @GetMapping("/{id}/balance")
    public ResponseEntity<CardBalanceDTO> getCardBalance(@PathVariable Long id) {
        log.info("Received request to get balance for card ID {}", id);
        CardBalanceDTO balance = userCardService.getCardBalance(id);
        return ResponseEntity.ok().body(balance);
    }

    @PostMapping("/{id}/block")
    public ResponseEntity<Void> blockCard(@PathVariable Long id) {
        log.info("Received request to block card ID {}", id);
        userCardService.blockCard(id);
        return ResponseEntity.ok().build();
    }
}
