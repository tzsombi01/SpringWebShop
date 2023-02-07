package com.tzsombi.webshop.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.YearMonth;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
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

    @Column(name = "user_id")
    private Long userId;
}
