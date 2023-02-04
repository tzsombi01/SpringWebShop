package com.tzsombi.webshop.models;

import java.util.List;

public record UserResponseDTO(
        String firstName,
        String lastName,
        String email,
        List<Product> sellingProducts,
        Role role
) {
}
