package com.tzsombi.webshop.models;

public record ComputerResponseDTO(
        Integer ramInGb,
        String manufacturer,
        Color color,
        GpuType gpu,
        CpuType cpu
) {
}
