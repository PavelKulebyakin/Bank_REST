package com.example.bankcards.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "cards")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CardEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    private String cardNumber;

    @Column(name = "last4digits")
    private String last4Digits;

    private String cardholderName;

    private LocalDateTime expirationDate;

    @Enumerated(EnumType.STRING)
    private CardStatus status;

    private BigDecimal balance;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.status = this.status == null ? CardStatus.ACTIVE : this.status;
        this.balance = this.balance == null ? BigDecimal.valueOf(0) : this.balance;
        if (this.cardNumber != null && this.cardNumber.length() >= 4) {
            this.last4Digits = this.cardNumber.substring(this.cardNumber.length() - 4);
        }
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
