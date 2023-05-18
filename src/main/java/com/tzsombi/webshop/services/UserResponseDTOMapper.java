package com.tzsombi.webshop.services;


import com.tzsombi.webshop.models.User;
import com.tzsombi.webshop.models.UserResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class UserResponseDTOMapper implements Function<User, UserResponseDTO> {

    private final CreditCardResponseDTOMapper creditCardResponseDTOMapper;

    @Override
    public UserResponseDTO apply(User user) {
        return new UserResponseDTO(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getSellingProducts(),
                user.getCards().stream().map(creditCardResponseDTOMapper).toList(),
                user.getRole()
        );
    }
}
