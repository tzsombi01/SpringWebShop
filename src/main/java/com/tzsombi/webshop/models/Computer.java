package com.tzsombi.webshop.models;

import com.fasterxml.jackson.annotation.JsonTypeName;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity(name = "Computer")
@DiscriminatorValue("Computer")
@JsonTypeName("phone")
public class Computer extends Product {

    private GpuType gpu;

    private CpuType cpu;

    private Integer ramInGb;

    private String manufacturer;

    private Color color;

    public Computer(String name, BigDecimal price, GpuType gpu, CpuType cpu, Integer ramInGb,
                    String manufacturer, Color color) {
        super(name, price);
        this.gpu = gpu;
        this.cpu = cpu;
        this.ramInGb = ramInGb;
        this.manufacturer = manufacturer;
        this.color = color;
    }
}
