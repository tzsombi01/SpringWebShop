package com.tzsombi.webshop.services;

import com.tzsombi.webshop.models.Product;
import com.tzsombi.webshop.repositories.ProductRepository;
import com.tzsombi.webshop.utils.ProductFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public void addProduct(String rawProduct) {
        Product product = ProductFactory.makeProduct(rawProduct);

        productRepository.save(product);
    }
}
