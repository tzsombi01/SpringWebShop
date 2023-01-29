package com.tzsombi.webshop.services;

import com.tzsombi.webshop.constanst.Constants;
import com.tzsombi.webshop.exceptions.AuthException;
import com.tzsombi.webshop.exceptions.UserNotFoundException;
import com.tzsombi.webshop.models.User;
import com.tzsombi.webshop.models.UserRequestDTO;
import com.tzsombi.webshop.models.UserResponseDTO;
import com.tzsombi.webshop.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserRequestDTOMapper userRequestDTOMapper;
    private final UserResponseDTOMapper userResponseDTOMapper;

    private final PasswordEncoder passwordEncoder;

    public UserResponseDTO getUserById(Long userId) {
        return userRepository.findById(userId)
                .map(userRequestDTOMapper)
                .orElseThrow(() -> new UserNotFoundException(Constants.USER_NOT_FOUND_MSG));
    }

    public UserResponseDTO updateUserById(Long userId, UserRequestDTO userRequestDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(Constants.USER_NOT_FOUND_MSG));

        String firstName = userRequestDTO.firstName();
        String lastName = userRequestDTO.lastName();
        String email = userRequestDTO.email();
        String password = userRequestDTO.password();

        if (firstName != null && firstName.length() > 0 && !user.getFirstName().equals(firstName)) {
            user.setFirstName(firstName);
        }

        if (lastName != null && lastName.length() > 0 && !user.getLastname().equals(lastName)) {
            user.setLastname(lastName);
        }

        if (email != null && email.length() > 0) {
            email = email.toLowerCase();
            if (userRepository.existsByEmail(email)) {
                throw new IllegalArgumentException(Constants.EMAIL_ALREADY_EXISTS_MSG);
            }
            Pattern pattern = Pattern.compile("^(.+)@(.+)$");
            if(!pattern.matcher(email).matches()) {
                throw new AuthException(Constants.INVALID_EMAIL_FORMAT_MSG);
            }
            user.setEmail(email);
        }

        if (password != null && password.length() > 0) {
            user.setPassword(passwordEncoder.encode(password));
        }

        userRepository.save(user);
        return userResponseDTOMapper.apply(user);
    }

    public void deleteUserById(Long userId) {
        userRepository.deleteById(userId);
    }
}
