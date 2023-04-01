package com.tzsombi.webshop.models;

public record PhoneResponseDTO(
        Integer ramInGb,
        String manufacturer,
        PhoneOperatingSystem system,
        Color color
) {
}
