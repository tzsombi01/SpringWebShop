package com.tzsombi.webshop.repositories;

import com.tzsombi.webshop.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
