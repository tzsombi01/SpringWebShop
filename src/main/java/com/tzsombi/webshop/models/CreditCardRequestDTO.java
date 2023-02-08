package com.tzsombi.webshop.models;

import java.time.YearMonth;

public record CreditCardRequestDTO(
        String cardNumber,
        YearMonth expiryDate,
        Boolean isActive,
        String fullName
) {
}
