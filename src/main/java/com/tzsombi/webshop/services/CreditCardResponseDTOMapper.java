package com.tzsombi.webshop.services;

import com.tzsombi.webshop.models.CreditCard;
import com.tzsombi.webshop.models.CreditCardResponseDTO;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class CreditCardResponseDTOMapper implements Function<CreditCard, CreditCardResponseDTO> {
    @Override
    public CreditCardResponseDTO apply(CreditCard creditCard) {
        return new CreditCardResponseDTO(
                creditCard.getId(),
                creditCard.getCardNumber(),
                creditCard.getExpiryDate(),
                creditCard.getFullName(),
                creditCard.getType(),
                creditCard.getIsActive()
        );
    }
}
