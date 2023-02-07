package com.tzsombi.webshop.models;


import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Builder
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    @OneToMany(
            targetEntity = Product.class,
            cascade = CascadeType.ALL
    )
    @JoinColumn(name = "seller_id", referencedColumnName = "id")
    @ToString.Exclude
    private List<Product> sellingProducts;

    @OneToMany(
            targetEntity = CreditCard.class,
            orphanRemoval = true
    )
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ToString.Exclude
    private List<CreditCard> cards;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public void addSellingProduct(Product product) {
        sellingProducts.add(product);
    }

    public void deleteSellingProduct(Product product) {
        sellingProducts.remove(product);
    }
}
