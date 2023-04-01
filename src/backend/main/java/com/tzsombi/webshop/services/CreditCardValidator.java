package com.tzsombi.webshop.services;

import com.tzsombi.webshop.constants.ErrorConstants;
import com.tzsombi.webshop.exceptions.CardNumberInvalidException;
import com.tzsombi.webshop.exceptions.ExpiryDateMustBeAfterCurrentDateException;
import com.tzsombi.webshop.models.CreditCard;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.YearMonth;
import java.time.ZonedDateTime;

@Service
public class CreditCardValidator {

    public static void luhnValidate(CreditCard creditCard, Clock clock) {
        int sum = 0;
        for (int i = creditCard.getCardNumber().length() - 1; i >= 0; i--) {
            int digit = Integer.parseInt(creditCard.getCardNumber().substring(i, i + 1));

            if ((creditCard.getCardNumber().length() - i) % 2 == 0) {
                digit = doubleGivenDigit(digit);
            }

            sum += digit;
        }

        if (! (sum % 10 == 0)) {
            throw new CardNumberInvalidException(ErrorConstants.CARD_NUMBER_INVALID);
        }

        if (creditCard.getExpiryDate().isBefore(YearMonth.from(ZonedDateTime.now(clock)))) {
            throw new ExpiryDateMustBeAfterCurrentDateException(ErrorConstants.EXPIRY_MUST_BE_AFTER_CURRENT_DATE_MSG);
        }
    }

    public static int doubleGivenDigit(int digit) {
        int doubledValue = digit * 2;

        int denominatorValueIfDigitBecomesBiggerThanNine = 9;
        if (doubledValue > 9) {
            doubledValue = digit - denominatorValueIfDigitBecomesBiggerThanNine;
        }

        return doubledValue;
    }

    public static void validateCardByVendor(CreditCard creditCard) {
        return;
    }
}
