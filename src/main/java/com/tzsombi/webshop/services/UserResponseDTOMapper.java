package com.tzsombi.webshop.services;


import com.tzsombi.webshop.models.User;
import com.tzsombi.webshop.models.UserResponseDTO;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class UserResponseDTOMapper implements Function<User, UserResponseDTO> {

    @Override
    public UserResponseDTO apply(User user) {
        return new UserResponseDTO(
                user.getFirstName(),
                user.getLastname(),
                user.getEmail(),
                user.getRole()
        );
    }
}
