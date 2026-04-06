package com.example.bankcards.controller.user;


import com.example.bankcards.dto.card.CardBalanceResponseDTO;
import com.example.bankcards.dto.card.CardInfoResponseDTO;
import com.example.bankcards.security.CustomUserDetailService;
import com.example.bankcards.security.CustomUserDetails;
import com.example.bankcards.security.JwtService;
import com.example.bankcards.service.UserCardService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.Mockito.times;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserCardController.class)
public class UserCardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserCardService userCardService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private CustomUserDetailService customUserDetailService;

    @Test
    void getUserCards_success() throws Exception {
        CustomUserDetails userDetails = new CustomUserDetails(1L, "user@example.com", "password", List.of());

        List<CardInfoResponseDTO> cards = List.of(
                new CardInfoResponseDTO(1L, "**** **** **** 1234", "John Doe", "12/28", "ACTIVE"),
                new CardInfoResponseDTO(2L, "**** **** **** 5678", "Jane Doe", "06/27", "BLOCKED")
        );

        Mockito.when(userCardService.getAllCards(1L)).thenReturn(cards);

        mockMvc.perform(get("/me/cards/all")
                        .with(authentication(
                                new UsernamePasswordAuthenticationToken(
                                        userDetails,
                                        null,
                                        userDetails.getAuthorities()
                                )
                        )))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(cards.size()))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[0].status").value("ACTIVE"))
                .andExpect(jsonPath("$[1].status").value("BLOCKED"));

        Mockito.verify(userCardService, times(1)).getAllCards(1L);
    }

    @Test
    void getUserCardsByStatusWithPagination_success() throws Exception {
        CustomUserDetails userDetails = new CustomUserDetails(1L, "user@example.com", "password", List.of());

        List<CardInfoResponseDTO> cards = List.of(
                new CardInfoResponseDTO(1L, "**** **** **** 1234", "John Doe", "12/28", "ACTIVE"),
                new CardInfoResponseDTO(2L, "**** **** **** 5678", "Jane Doe", "06/27", "ACTIVE")
        );

        Mockito.when(userCardService.getAllCardsWithStatusAndPaging(1L, "ACTIVE", 1, 10))
                .thenReturn(cards);

        mockMvc.perform(get("/me/cards")
                        .param("status", "ACTIVE")
                        .param("page", "1")
                        .param("size", "10")
                        .with(authentication(
                                new UsernamePasswordAuthenticationToken(
                                        userDetails,
                                        null,
                                        userDetails.getAuthorities()
                                )
                        )))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(cards.size()))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].status").value("ACTIVE"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].status").value("ACTIVE"));

        Mockito.verify(userCardService, times(1))
                .getAllCardsWithStatusAndPaging(1L, "ACTIVE", 1, 10);
    }

    @Test
    void getUserCardsByPan_success() throws Exception {
        CustomUserDetails userDetails = new CustomUserDetails(1L, "user@example.com", "password", List.of());

        CardInfoResponseDTO card = new CardInfoResponseDTO(1L, "**** **** **** 1234", "John Doe", "12/28", "ACTIVE");

        Mockito.when(userCardService.getAllCardsByPanLastDigits(1L, "1234")).thenReturn(card);

        mockMvc.perform(get("/me/cards/pan")
                        .param("last_4", "1234")
                        .with(authentication(
                                new UsernamePasswordAuthenticationToken(
                                        userDetails,
                                        null,
                                        userDetails.getAuthorities()
                                )
                        )))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.maskedNumber").value("**** **** **** 1234"))
                .andExpect(jsonPath("$.cardholderName").value("John Doe"))
                .andExpect(jsonPath("$.status").value("ACTIVE"));

        Mockito.verify(userCardService, times(1))
                .getAllCardsByPanLastDigits(1L, "1234");
    }

    @Test
    void getUserCardsById_success() throws Exception {
        CustomUserDetails userDetails = new CustomUserDetails(1L, "user@example.com", "password", List.of());

        CardInfoResponseDTO card = new CardInfoResponseDTO(1L, "**** **** **** 1234", "John Doe", "12/28", "ACTIVE");

        Mockito.when(userCardService.getCardsById(1L, 1L)).thenReturn(card);

        mockMvc.perform(get("/me/cards/1")
                        .with(authentication(
                                new UsernamePasswordAuthenticationToken(
                                        userDetails,
                                        null,
                                        userDetails.getAuthorities()
                                )
                        )))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.maskedNumber").value("**** **** **** 1234"))
                .andExpect(jsonPath("$.cardholderName").value("John Doe"))
                .andExpect(jsonPath("$.status").value("ACTIVE"));

        Mockito.verify(userCardService, times(1))
                .getCardsById(1L, 1L);
    }

    @Test
    void getCardBalance_success() throws Exception {
        Long userId = 1L;
        Long cardId = 100L;
        CustomUserDetails userDetails = new CustomUserDetails(userId, "user@example.com", "password", List.of());

        CardBalanceResponseDTO balanceDto = CardBalanceResponseDTO.builder()
                .cardId(cardId)
                .balance(BigDecimal.valueOf(1234.56))
                .build();

        Mockito.when(userCardService.getCardBalance(userId, cardId)).thenReturn(balanceDto);

        mockMvc.perform(get("/me/cards/{cardId}/balance", cardId)
                        .with(authentication(
                                new UsernamePasswordAuthenticationToken(
                                        userDetails,
                                        null,
                                        userDetails.getAuthorities()
                                )
                        )))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cardId").value(cardId))
                .andExpect(jsonPath("$.balance").value(1234.56));

        Mockito.verify(userCardService, times(1))
                .getCardBalance(userId, cardId);
    }

    @Test
    void blockCard_success() throws Exception {
        CustomUserDetails userDetails = new CustomUserDetails(1L, "user@example.com", "password", List.of());

        Mockito.doNothing().when(userCardService).blockCard(1L, 100L);

        mockMvc.perform(post("/me/cards/{cardId}/block", 100L)
                        .with(authentication(
                                new UsernamePasswordAuthenticationToken(
                                        userDetails,
                                        null,
                                        userDetails.getAuthorities()
                                )
                        ))
                        .with(csrf()))
                .andExpect(status().isOk());

        Mockito.verify(userCardService, times(1)).blockCard(1L, 100L);
    }
}
