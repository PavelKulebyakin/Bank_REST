package com.example.bankcards.controller.admin;

import com.example.bankcards.dto.card.CardCreateDTO;
import com.example.bankcards.dto.card.CardInfoResponseDTO;
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
    public ResponseEntity<CardInfoResponseDTO> addNewCard(@RequestBody @Valid CardCreateDTO dto) {
        log.info("Received request to create a new card with data : {}", dto.toString());
        CardInfoResponseDTO newCard = adminCardService.addNewCard(dto);
        return ResponseEntity.created(URI.create("/admin/cards/" + newCard.id())).body(newCard);
    }

    @GetMapping("/all")
    public ResponseEntity<List<CardInfoResponseDTO>> getAllCards() {
        log.info("Received request to get all cards");
        List<CardInfoResponseDTO> cards = adminCardService.getAllCards();
        return ResponseEntity.ok().body(cards);
    }

    @GetMapping
    public ResponseEntity<List<CardInfoResponseDTO>> getCardsWithStatusAndPaging(
        log.info("Received request to get all cards with page : {}, size : {}, status : {}", page, size, status);
        List<CardInfoResponseDTO> cards = adminCardService.getAllCardsWithStatusAndPaging(status, page, size);
        return ResponseEntity.ok().body(cards);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CardInfoResponseDTO> getCardById(@PathVariable @Min(1) Long id) {
        log.info("Received request to get card with id : {}", id);
        CardInfoResponseDTO card = adminCardService.getCardById(id);
        return ResponseEntity.ok().body(card);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCardById(@PathVariable Long id) {
        log.info("Received request to delete card with id : {}", id);
        adminCardService.deleteCardById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<CardInfoResponseDTO> updateCardStatusById(
        log.info("Received request to update card status with id : {}, new status : {}", id, dto.getStatus());
        CardInfoResponseDTO card = adminCardService.updateCardStatusById(id, dto);
        return ResponseEntity.ok().body(card);
    }
}
