package com.tzsombi.webshop.repositories;

import com.tzsombi.webshop.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query(value = "SELECT p.product_type FROM products p WHERE p.id = ?1", nativeQuery = true)
    String findProductTypeById(Long productId);
}
