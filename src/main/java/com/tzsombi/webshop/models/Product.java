package com.tzsombi.webshop.models;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Type;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Entity(name = "products")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "product_type")
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "type"
)
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = Phone.class, name = "phone"),
                @JsonSubTypes.Type(value = Computer.class, name = "computer")
        }
)
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private BigDecimal price;

    @ManyToMany
    @JoinTable(
            name = "user_product",
            joinColumns = {
                    @JoinColumn(
                            name = "user_id",
                            referencedColumnName = "id")
            },
            inverseJoinColumns = {
                    @JoinColumn(
                            name = "product_id",
                            referencedColumnName = "id"
                    )
            }
    )
    @ToString.Exclude
    Set<User> users = new HashSet<>();

    public Product(String name, BigDecimal price) {
        this.name = name;
        this.price = price;
    }
}
