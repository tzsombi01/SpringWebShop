package com.tzsombi.webshop.models;

import java.time.YearMonth;

public record CreditCardResponseDTO(
        String cardNumber,
        YearMonth expiryDate,
        String fullName,
        Boolean isActive
) {
}
