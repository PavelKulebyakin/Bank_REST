package com.example.bankcards.controller.user;

import com.example.bankcards.dto.transfer.TransferDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/me/cards")
@Log4j2
public class UserCardController {

    @GetMapping
    public ResponseEntity<Void> getUserCards() {
        log.info("Resieved request to get all cards");
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<Void> getUserCardsWithPaging(@RequestParam Integer page, @RequestParam Integer size) {
        log.info("Resieved request to get all cards with page {} and size {}", page, size);
        return getUserCards();
    }

    @GetMapping(params = "status")
    public ResponseEntity<Void> getUserCardsByStatus(@RequestParam String status) {
        log.info("Received request to get cards with status {}", status);
        return getUserCards();
    }

    @GetMapping(params = "pan_last_4")
    public ResponseEntity<Void> getUserCardsByPan(@RequestParam("pan_last_4") String panLast4) {
        log.info("Received request to get cards with last 4 digits {}", panLast4);
        return getUserCards();
    }

    @GetMapping(params = "id")
    public ResponseEntity<Void> getUserCardById(@RequestParam Long id) {
        log.info("Received request to get card with ID {}", id);
        return getUserCards();
    }

    @PostMapping("/transfer")
    public ResponseEntity<Void> transferBetweenCards(@RequestBody TransferDTO dto) {
        log.info("Received transfer request from {} to {} amount {}", dto.getFromId(), dto.getToId(), dto.getAmount());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/balance")
    public ResponseEntity<Void> getCardBalance(@PathVariable Long id) {
        log.info("Received request to get balance for card ID {}", id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/block")
    public ResponseEntity<Void> blockCard(@PathVariable Long id) {
        log.info("Received request to block card ID {}", id);
        return ResponseEntity.ok().build();
    }
}
