package com.tzsombi.webshop.models;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "products")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "product_type")
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "type"
)
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = Phone.class, name = "Phone"),
                @JsonSubTypes.Type(value = Computer.class, name = "Computer")
        }
)
public abstract class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private BigDecimal price;

    private String description;

    @Column(name = "seller_id")
    private Long sellerId;

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
    Set<User> buyers = new HashSet<>();

    public Product(String name, BigDecimal price, String description, Long sellerId) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.sellerId = sellerId;
    }

    public void addBuyer(User user) {
        buyers.add(user);
    }

    public void deleteBuyer(User user) {
        buyers.remove(user);
    }
}
