package com.tzsombi.webshop.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.YearMonth;
import java.util.Objects;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "credit_cards")
public class CreditCard {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "card_number", nullable = false)
    private String cardNumber;

    @Column(name = "expiry_date", nullable = false)
    private YearMonth expiryDate;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @Column(name = "user_id")
    private Long userId;

    @Transient
    public CardType getType() {
        return CardType.detect(cardNumber);
    }


    public CreditCard(String cardNumber, YearMonth expiryDate, String fullName, Boolean isActive, Long userId) {
        this.cardNumber = cardNumber;
        this.expiryDate = expiryDate;
        this.fullName = fullName;
        this.isActive = isActive;
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CreditCard that = (CreditCard) o;
        return Objects.equals(id, that.id) && Objects.equals(cardNumber, that.cardNumber) && Objects.equals(expiryDate, that.expiryDate) && Objects.equals(fullName, that.fullName) && Objects.equals(isActive, that.isActive) && Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, cardNumber, expiryDate, fullName, isActive, userId);
    }
}
