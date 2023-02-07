package com.tzsombi.webshop.services;

import com.tzsombi.webshop.models.Product;
import com.tzsombi.webshop.models.ProductResponseDTO;
import com.tzsombi.webshop.models.User;
import org.springframework.stereotype.Service;

import java.util.function.BiFunction;

@Service
public class ProductResponseDTOMapper implements BiFunction<Product, User, ProductResponseDTO> {

    @Override
    public ProductResponseDTO apply(Product product, User user) {
        return new ProductResponseDTO(
                product.getName(),
                product.getPrice(),
                product.getDescription(),
                user.getEmail()
        );
    }
}
