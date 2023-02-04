package com.tzsombi.webshop.services;

import com.tzsombi.webshop.models.Product;
import com.tzsombi.webshop.repositories.ProductRepository;
import com.tzsombi.webshop.utils.ProductFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public Page<Product> getAllProducts(Integer page, Integer numberOfProducts, String sortBy, Boolean isOrderAsc) {
        return productRepository.findAll(
                PageRequest.of(page, numberOfProducts, isOrderAsc ? Sort.Direction.ASC : Sort.Direction.DESC, sortBy));
    }

    public void addProduct(String rawProduct) {
        Product product = ProductFactory.makeProduct(rawProduct);

        productRepository.save(product);
    }
}
