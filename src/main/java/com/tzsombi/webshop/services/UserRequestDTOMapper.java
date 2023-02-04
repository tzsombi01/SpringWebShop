package com.tzsombi.webshop.services;

import com.tzsombi.webshop.models.User;
import com.tzsombi.webshop.models.UserRequestDTO;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class UserRequestDTOMapper implements Function<User, UserRequestDTO> {

    @Override
    public UserRequestDTO apply(User user) {
        return new UserRequestDTO(
                user.getFirstName(),
                user.getLastname(),
                user.getEmail(),
                user.getPassword()
        );
    }
}
