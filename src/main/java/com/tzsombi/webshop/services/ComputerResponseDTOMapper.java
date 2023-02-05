package com.tzsombi.webshop.services;

import com.tzsombi.webshop.models.Computer;
import com.tzsombi.webshop.models.ComputerResponseDTO;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class ComputerResponseDTOMapper implements Function<Computer, ComputerResponseDTO> {

    @Override
    public ComputerResponseDTO apply(Computer computer) {
        return new ComputerResponseDTO(
                computer.getRamInGb(),
                computer.getManufacturer(),
                computer.getColor(),
                computer.getGpu(),
                computer.getCpu()
        );
    }
}
