package com.tzsombi.webshop.models;

import java.time.YearMonth;

public record CreditCardResponseDTO(
        Long id,
        String cardNumber,
        YearMonth expiryDate,
        String fullName,
        CardType type,
        Boolean isActive
) {
}
