package com.tzsombi.webshop.models;

import java.math.BigDecimal;

public record ProductRequestDTO(
        String name,
        BigDecimal price,
        String description,
        Integer ramInGb,
        String manufacturer,
        PhoneOperatingSystem phoneOperatingSystem,
        ComputerOperatingSystem computerOperatingSystem,
        Color color,
        GpuType gpu,
        CpuType cpu
) {
}
