package com.example.bankcards.controller.admin;

import com.example.bankcards.dto.card.CardCreateDTO;
import com.example.bankcards.dto.card.CardResponseDTO;
import com.example.bankcards.dto.card.CardStatusUpdateDTO;
import com.example.bankcards.service.AdminCardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/admin/cards")
@RequiredArgsConstructor
@Log4j2
public class AdminCardController {

    private final AdminCardService adminCardService;

    @PostMapping
    public ResponseEntity<CardResponseDTO> addNewCard(@RequestBody CardCreateDTO dto) {
        log.info("Received request to create a new card with data : {}", dto.toString());
        CardResponseDTO newCard = adminCardService.addNewCard(dto);
        return ResponseEntity.created(URI.create("/admin/cards" + newCard.getId())).body(newCard);
    }

    @GetMapping("/all")
    public ResponseEntity<List<CardResponseDTO>> getAllCards() {
        log.info("Received request to get all cards");
        List<CardResponseDTO> cards = adminCardService.getAllCards();
        return ResponseEntity.ok().body(cards);
    }

    @GetMapping
    public ResponseEntity<List<CardResponseDTO>> getAllCardsWithStatusAndPaging(
            @RequestParam int page,
            @RequestParam int size,
            @RequestParam String status) {
        log.info("Received request to get all cards with page : {}, size : {}, status : {}", page, size, status);
        List<CardResponseDTO> cards = adminCardService.getAllCardsWithStatusAndPaging(status, page, size);
        return ResponseEntity.ok().body(cards);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CardResponseDTO> getCardById(@PathVariable Long id) {
        log.info("Received request to get card with id : {}", id);
        CardResponseDTO card = adminCardService.getCardById(id);
        return ResponseEntity.ok().body(card);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCardById(@PathVariable Long id) {
        log.info("Received request to delete card with id : {}", id);
        adminCardService.deleteCardById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<CardResponseDTO> updateCardStatusById(
            @PathVariable Long id,
            @RequestBody CardStatusUpdateDTO dto) {
        log.info("Received request to update card status with id : {}, new status : {}", id, dto.getStatus());
        CardResponseDTO card = adminCardService.updateCardStatusById(id, dto);
        return ResponseEntity.ok().body(card);
    }
}
