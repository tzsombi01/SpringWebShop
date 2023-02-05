package com.tzsombi.webshop.models;

import java.math.BigDecimal;

public record ProductResponseDTO(
        String name,
        BigDecimal price,
        String email
) {
}
