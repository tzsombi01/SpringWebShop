package com.tzsombi.webshop.services;

import com.tzsombi.webshop.models.Phone;
import com.tzsombi.webshop.models.PhoneResponseDTO;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class PhoneResponseDTOMapper implements Function<Phone, PhoneResponseDTO> {

    @Override
    public PhoneResponseDTO apply(Phone phone) {
        return new PhoneResponseDTO(
                phone.getRamInGb(),
                phone.getManufacturer(),
                phone.getSystem(),
                phone.getColor()
        );
    }
}
