package com.tzsombi.webshop.utils;

import com.tzsombi.webshop.constants.Constants;
import com.tzsombi.webshop.exceptions.AuthException;
import com.tzsombi.webshop.repositories.UserRepository;

public class CredentialChecker {

    public static void ifUserPresentWithEmailThrowAuthException(String email, UserRepository userRepository) {
        if(userRepository.existsByEmail(email)) {
            throw new AuthException(Constants.EMAIL_ALREADY_EXISTS_MSG);
        }
    }
}
