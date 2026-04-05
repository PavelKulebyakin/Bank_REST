package com.example.bankcards.mapper;

import com.example.bankcards.dto.card.CardInfoResponseDTO;
import com.example.bankcards.entity.CardEntity;
import com.example.bankcards.entity.CardStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public interface CardMapper {

    @Mapping(target = "expirationDate", source = "expirationDate", qualifiedByName = "expirationDateToString")
    @Mapping(target = "status", source = "status", qualifiedByName = "statusToString")
    CardInfoResponseDTO toDto(CardEntity card);

    @Named("statusToString")
    static String statusToString(CardStatus status) {
        return status != null ? status.name() : null;
    }

    @Named("expirationDateToString")
    static String formatExpirationDate(LocalDateTime expirationDate) {
        return expirationDate.format(DateTimeFormatter.ofPattern("MM/yy"));
    }
}
