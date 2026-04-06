package com.example.bankcards.controller.user;

import com.example.bankcards.dto.transfer.TransferDTO;
import com.example.bankcards.security.CustomUserDetailService;
import com.example.bankcards.security.CustomUserDetails;
import com.example.bankcards.security.JwtService;
import com.example.bankcards.service.TransferService;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.junit.jupiter.api.Test;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(TransferController.class)
class TransectionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TransferService transferService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private CustomUserDetailService customUserDetailService;

    @Test
    void transferBetweenCards_success() throws Exception {
        Long userId = 1L;

        CustomUserDetails userDetails = new CustomUserDetails(
                userId,
                "user@example.com",
                "password",
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );

        String JSON_PAYLOAD = """
        {
            "fromId": 100,
            "toId": 200,
            "amount": 500.00
        }
        """;

        Mockito.doNothing().when(transferService).processTransfer(eq(userId), Mockito.any(TransferDTO.class));

        mockMvc.perform(post("/me/transfer")
                        .with(authentication(
                                new UsernamePasswordAuthenticationToken(
                                        userDetails,
                                        null,
                                        userDetails.getAuthorities()
                                )
                        ))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JSON_PAYLOAD))
                .andExpect(status().isOk());


        Mockito.verify(transferService, times(1))
                .processTransfer(eq(userId), Mockito.any(TransferDTO.class));
    }
}
