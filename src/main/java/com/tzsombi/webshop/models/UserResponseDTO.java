package com.tzsombi.webshop.models;

import java.util.List;

public record UserResponseDTO(
        Long id,
        String firstName,
        String lastName,
        String email,
        List<Product> sellingProducts,
        List<CreditCardResponseDTO> creditCards,
        Role role
) {
}
