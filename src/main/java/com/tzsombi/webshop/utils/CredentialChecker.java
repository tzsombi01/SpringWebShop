package com.tzsombi.webshop.utils;

import com.tzsombi.webshop.constants.ErrorConstants;
import com.tzsombi.webshop.exceptions.AuthException;
import com.tzsombi.webshop.exceptions.CanNotBuyOwnProductExeption;
import com.tzsombi.webshop.models.CreditCard;
import com.tzsombi.webshop.models.Product;
import com.tzsombi.webshop.models.User;
import com.tzsombi.webshop.repositories.UserRepository;

public class CredentialChecker {

    public static void ifUserPresentWithEmailThrowAuthException(String email, UserRepository userRepository) {
        if (userRepository.existsByEmail(email)) {
            throw new AuthException(ErrorConstants.EMAIL_ALREADY_EXISTS_MSG);
        }
    }

    public static void ifSellerAndBuyerIsTheSameThrowException(Product product, User user) {
        if (product.getSellerId().equals(user.getId())) {
            throw new CanNotBuyOwnProductExeption(ErrorConstants.CANNOT_BUY_YOUR_OWN_PRODUCT_MSG);
        }
    }

    public static void ifUserHasTheCardProceedOrElseException(User user, CreditCard creditCard) {
        if (user.getCards().stream().noneMatch(card -> card.getId().equals(creditCard.getId()))) {
            throw new AuthException(ErrorConstants.NO_PERMISSION_TO_MODIFY_CARD);
        }
    }
}
