package com.tzsombi.webshop.repositories;

import com.tzsombi.webshop.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query(value = "SELECT u.email FROM User u INNER JOIN Product p ON u.id = p.id WHERE p.id = ?1")
    String findUserEmailByProductId(Long productId);
}
