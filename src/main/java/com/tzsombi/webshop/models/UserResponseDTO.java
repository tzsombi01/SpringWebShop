package com.tzsombi.webshop.models;

public record UserResponseDTO(
        String firstName,
        String lastName,
        String email,
        Role role
) {
}
