package com.tzsombi.webshop.auth;


import com.tzsombi.webshop.constants.Constants;
import com.tzsombi.webshop.exceptions.AuthException;
import com.tzsombi.webshop.models.Role;
import com.tzsombi.webshop.models.User;
import com.tzsombi.webshop.repositories.UserRepository;
import com.tzsombi.webshop.utils.CredentialChecker;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
        String firstName = request.getFirstName();
        if(firstName == null || firstName.length() == 0) {
            throw new IllegalArgumentException(Constants.NAMES_MUST_BE_AT_LEAST_1_CHAR_LONG_MSG);
        }

        String lastName = request.getLastName();
        if(lastName == null || lastName.length() == 0) {
            throw new IllegalArgumentException(Constants.NAMES_MUST_BE_AT_LEAST_1_CHAR_LONG_MSG);
        }

        String email = request.getEmail();
        if(email != null && email.length() > 0) {
            email = email.toLowerCase();

            Pattern pattern = Pattern.compile("^(.+)@(.+)$");
            if(!pattern.matcher(email).matches()) {
                throw new AuthException(Constants.INVALID_EMAIL_FORMAT_MSG);
            }

            CredentialChecker.ifUserPresentWithEmailThrowAuthException(email, userRepository);
        }

        User user = User
                .builder()
                .firstName(firstName)
                .lastname(lastName)
                .email(email)
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();

        userRepository.save(user);

        String jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        String email = request.getEmail();
        if(email != null && email.length() > 0) {
            email = email.toLowerCase();

            Pattern pattern = Pattern.compile("^(.+)@(.+)$");
            if(!pattern.matcher(email).matches()) {
                throw new AuthException(Constants.INVALID_EMAIL_FORMAT_MSG);
            }
        }

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException(Constants.USER_NOT_FOUND_MSG));
        String jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}
