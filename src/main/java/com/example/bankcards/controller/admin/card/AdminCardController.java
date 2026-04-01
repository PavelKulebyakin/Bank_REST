package com.example.bankcards.controller.admin.card;

import com.example.bankcards.dto.card.CardCreateDTO;
import com.example.bankcards.dto.card.CardStatusUpdateDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/cards")
@Log4j2
public class AdminCardController {

    @PostMapping
    public ResponseEntity<Void> addNewCard(@RequestBody CardCreateDTO dto) {
        log.info("Received request to create a new card with data : {}", dto.toString());
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<Void> getAllCards() {
        log.info("Received request to get all cards");
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<Void> getAllCardsWithPaging(@RequestParam int page, @RequestParam int size) {
        log.info("Received request to get all cards with page : {} and size : {}", page, size);
        return getAllCards();
    }

    @GetMapping
    public ResponseEntity<Void> getAllCardsWithStatus(@RequestParam String status) {
        log.info("Received request to get all cards with status : {}", status);
        return getAllCards();
    }

    @GetMapping
    public ResponseEntity<Void> getAllCardsWithStatusAndPaging(
            @RequestParam int page, @RequestParam int size, @RequestParam String status) {
        log.info("Received request to get all cards with page : {}, size : {}, status : {}", page, size, status);
        return getAllCards();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Void> getCardById(@PathVariable Long id) {
        log.info("Received request to get card with id : {}", id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCardById(@PathVariable Long id) {
        log.info("Received request to delete card with id : {}", id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateCardStatusById(@PathVariable Long id, @RequestBody CardStatusUpdateDTO dto) {
        log.info("Received request to update card status with id : {}, new status : {}", id, dto.getStatus());
        return ResponseEntity.ok().build();
    }
}
