package com.tzsombi.webshop.models;

import com.fasterxml.jackson.annotation.JsonTypeName;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity(name = "Phone")
@DiscriminatorValue("Phone")
@JsonTypeName("phone")
public class Phone extends Product {

    private Integer ramInGb;

    private String manufacturer;

    private PhoneOperatingSystem system;

    private Color color;

    public Phone(String name, BigDecimal price, Integer ramInGb, String manufacturer,
                 PhoneOperatingSystem system, Color color) {
        super(name, price);
        this.ramInGb = ramInGb;
        this.manufacturer = manufacturer;
        this.system = system;
        this.color = color;
    }
}