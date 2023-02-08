package com.tzsombi.webshop.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.tzsombi.webshop.models.CreditCard;
import com.tzsombi.webshop.models.CreditCardRequestDTO;

public class CreditCardFactory {

    public static CreditCard makeCard(CreditCardRequestDTO cardRequestDTO) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        return mapper.convertValue(cardRequestDTO, CreditCard.class);
    }
}
