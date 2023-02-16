package com.tzsombi.webshop.services;

import com.tzsombi.webshop.constants.ErrorConstants;
import com.tzsombi.webshop.exceptions.ExpiryDateMustBeAfterCurrentDateException;
import com.tzsombi.webshop.models.CreditCard;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.YearMonth;
import java.time.ZonedDateTime;

@Service
public class CreditCardValidator {

    public static void validate(CreditCard creditCard, Clock clock) {
        if (creditCard.getExpiryDate().isBefore(YearMonth.from(ZonedDateTime.now(clock)))) {
            throw new ExpiryDateMustBeAfterCurrentDateException(ErrorConstants.EXPIRY_MUST_BE_AFTER_CURRENT_DATE_MSG);
        }
    }
}
