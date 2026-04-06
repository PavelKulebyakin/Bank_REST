package com.example.bankcards.controller.admin;

import com.example.bankcards.dto.card.CardCreateDTO;
import com.example.bankcards.dto.card.CardInfoResponseDTO;
import com.example.bankcards.dto.card.CardStatusUpdateDTO;
import com.example.bankcards.security.CustomUserDetailService;
import com.example.bankcards.security.JwtService;
import com.example.bankcards.service.AdminCardService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdminCardController.class)
@AutoConfigureMockMvc(addFilters = false)
public class AdminCardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AdminCardService adminCardService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private CustomUserDetailService userDetailService;

    @Test
    void addNewCard_success() throws Exception {
        CardInfoResponseDTO responseDTO = new CardInfoResponseDTO(
                1L,
                "**** **** **** 1234",
                "John Doe",
                "12/28",
                "ACTIVE"
        );

        Mockito.when(adminCardService.addNewCard(Mockito.any(CardCreateDTO.class)))
                .thenReturn(responseDTO);

        String JSON_PAYLOAD = """
        {
            "userId": 1,
            "cardholderName": "John Doe",
            "validityPeriod": 4
        }
        """;
        mockMvc.perform(post("/admin/cards")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JSON_PAYLOAD))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/admin/cards/1"))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.maskedNumber").value("**** **** **** 1234"))
                .andExpect(jsonPath("$.cardholderName").value("John Doe"))
                .andExpect(jsonPath("$.expirationDate").value("12/28"))
                .andExpect(jsonPath("$.status").value("ACTIVE"));

        Mockito.verify(adminCardService, times(1))
                .addNewCard(Mockito.any(CardCreateDTO.class));
    }

    @Test
    void getAllCards_success() throws Exception {
        List<CardInfoResponseDTO> cardList = List.of(
                new CardInfoResponseDTO(100L, "**** **** **** 1234", "John Doe", "12/28", "ACTIVE"),
                new CardInfoResponseDTO(101L, "**** **** **** 5678", "Jane Smith", "06/27", "ACTIVE")
        );

        Mockito.when(adminCardService.getAllCards()).thenReturn(cardList);

        mockMvc.perform(get("/admin/cards/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(cardList.size()))
                .andExpect(jsonPath("$[0].id").value(100))
                .andExpect(jsonPath("$[0].maskedNumber").value("**** **** **** 1234"))
                .andExpect(jsonPath("$[0].cardholderName").value("John Doe"))
                .andExpect(jsonPath("$[0].expirationDate").value("12/28"))
                .andExpect(jsonPath("$[0].status").value("ACTIVE"))
                .andExpect(jsonPath("$[1].id").value(101))
                .andExpect(jsonPath("$[1].maskedNumber").value("**** **** **** 5678"))
                .andExpect(jsonPath("$[1].cardholderName").value("Jane Smith"))
                .andExpect(jsonPath("$[1].expirationDate").value("06/27"))
                .andExpect(jsonPath("$[1].status").value("ACTIVE"));

        Mockito.verify(adminCardService, times(1)).getAllCards();
    }

    @Test
    void getCardsWithStatusAndPaging_success() throws Exception {
        List<CardInfoResponseDTO> cardList = List.of(
                new CardInfoResponseDTO(100L, "1234", "John Doe", "12/28", "ACTIVE"),
                new CardInfoResponseDTO(101L, "5678", "Jane Smith", "06/27", "ACTIVE")
        );

        String statusParam = "ACTIVE";
        int pageParam = 1;
        int sizeParam = 10;

        Mockito.when(adminCardService.getAllCardsWithStatusAndPaging(statusParam, pageParam, sizeParam))
                .thenReturn(cardList);

        mockMvc.perform(get("/admin/cards")
                        .param("status", statusParam)
                        .param("page", String.valueOf(pageParam))
                        .param("size", String.valueOf(sizeParam))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(cardList.size()))
                .andExpect(jsonPath("$[0].id").value(100))
                .andExpect(jsonPath("$[0].status").value("ACTIVE"))
                .andExpect(jsonPath("$[1].id").value(101))
                .andExpect(jsonPath("$[1].status").value("ACTIVE"));

        Mockito.verify(adminCardService, times(1))
                .getAllCardsWithStatusAndPaging(statusParam, pageParam, sizeParam);
    }

    @Test
    void getCardById_success() throws Exception {
        Long cardId = 100L;
        CardInfoResponseDTO cardDTO = new CardInfoResponseDTO(
                cardId,
                "**** **** **** 1234",
                "John Doe",
                "12/28",
                "ACTIVE"
        );

        Mockito.when(adminCardService.getCardById(cardId)).thenReturn(cardDTO);

        mockMvc.perform(get("/admin/cards/{id}", cardId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(cardId))
                .andExpect(jsonPath("$.maskedNumber").value("**** **** **** 1234"))
                .andExpect(jsonPath("$.cardholderName").value("John Doe"))
                .andExpect(jsonPath("$.expirationDate").value("12/28"))
                .andExpect(jsonPath("$.status").value("ACTIVE"));

        Mockito.verify(adminCardService, times(1)).getCardById(cardId);
    }

    @Test
    void deleteCardById_success() throws Exception {
        Long cardId = 100L;

        Mockito.doNothing().when(adminCardService).deleteCardById(cardId);
        mockMvc.perform(delete("/admin/cards/{id}", cardId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        Mockito.verify(adminCardService, times(1)).deleteCardById(cardId);
    }

    @Test
    void updateCardStatusById_success() throws Exception {
        String JSON_PAYLOAD = """
        {
            "status": "BLOCKED"
        }
        """;

        Long cardId = 100L;

        CardInfoResponseDTO updatedCard = new CardInfoResponseDTO(
                cardId,
                "**** **** **** 1234",
                "John Doe",
                "12/28",
                "BLOCKED"
        );

        Mockito.when(adminCardService.updateCardStatusById(Mockito.eq(cardId), Mockito.any(CardStatusUpdateDTO.class)))
                .thenReturn(updatedCard);

        mockMvc.perform(put("/admin/cards/{id}/status", cardId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JSON_PAYLOAD))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(cardId))
                .andExpect(jsonPath("$.maskedNumber").value("**** **** **** 1234"))
                .andExpect(jsonPath("$.cardholderName").value("John Doe"))
                .andExpect(jsonPath("$.expirationDate").value("12/28"))
                .andExpect(jsonPath("$.status").value("BLOCKED"));

        Mockito.verify(adminCardService, times(1))
                .updateCardStatusById(Mockito.eq(cardId), Mockito.any(CardStatusUpdateDTO.class));
    }
}

