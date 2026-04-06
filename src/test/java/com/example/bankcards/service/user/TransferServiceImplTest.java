package com.example.bankcards.service.user;

import com.example.bankcards.config.EncryptionConfig;
import com.example.bankcards.dto.transfer.TransferDTO;
import com.example.bankcards.entity.CardEntity;
import com.example.bankcards.entity.TransferEntity;
import com.example.bankcards.entity.UserEntity;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.TransferRepository;
import com.example.bankcards.service.TransferService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Optional;

@SpringBootTest
class TransferServiceImplTest {

    @Mock
    private CardRepository cardRepository;

    @Mock
    private TransferRepository transferRepository;

    @InjectMocks
    private TransferService transferService;

    @Mock
    private EncryptionConfig encryptionConfig;

    @Test
    void processTransfer_success() {
        Long userId = 1L;
        Long fromCardId = 100L;
        Long toCardId = 200L;
        BigDecimal amount = BigDecimal.valueOf(500);

        CardEntity fromCard = CardEntity.builder()
                .id(fromCardId)
                .balance(BigDecimal.valueOf(1000))
                .user(UserEntity.builder().id(userId).build())
                .build();

        CardEntity toCard = CardEntity.builder()
                .id(toCardId)
                .balance(BigDecimal.valueOf(200))
                .build();

        TransferDTO dto = new TransferDTO(fromCardId, toCardId, amount);

        Mockito.when(cardRepository.findByIdAndUserId(fromCardId, userId))
                .thenReturn(Optional.of(fromCard));
        Mockito.when(cardRepository.findById(toCardId))
                .thenReturn(Optional.of(toCard));
        Mockito.when(cardRepository.save(Mockito.any())).thenAnswer(invocation -> invocation.getArgument(0));
        Mockito.when(transferRepository.save(Mockito.any())).thenAnswer(invocation -> invocation.getArgument(0));

        transferService.processTransfer(userId, dto);

        Assertions.assertEquals(BigDecimal.valueOf(500), fromCard.getBalance());
        Assertions.assertEquals(BigDecimal.valueOf(700), toCard.getBalance());

        Mockito.verify(cardRepository, Mockito.times(2)).save(Mockito.any(CardEntity.class));
        Mockito.verify(transferRepository, Mockito.times(1)).save(Mockito.any(TransferEntity.class));
    }
}
