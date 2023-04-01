package com.tzsombi.webshop.models;

public record UserRequestDTO(
        String firstName,
        String lastName,
        String email,
        String password
) {
}
