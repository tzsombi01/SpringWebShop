package com.tzsombi.webshop.models;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
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

    @ManyToMany(
            fetch = FetchType.EAGER
    )
    @JoinTable(
            name = "user_product",
            joinColumns = {
                    @JoinColumn(
                            name = "product_id",
                            referencedColumnName = "id")
            },
            inverseJoinColumns = {
                    @JoinColumn(
                            name = "user_id",
                            referencedColumnName = "id"
                    )
            }
    )
    @ToString.Exclude
    List<User> buyers = new ArrayList<>();

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
