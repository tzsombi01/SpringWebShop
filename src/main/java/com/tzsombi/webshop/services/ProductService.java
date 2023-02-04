package com.tzsombi.webshop.services;

import com.tzsombi.webshop.constants.Constants;
import com.tzsombi.webshop.exceptions.UserNotFoundException;
import com.tzsombi.webshop.models.Product;
import com.tzsombi.webshop.models.User;
import com.tzsombi.webshop.repositories.ProductRepository;
import com.tzsombi.webshop.repositories.UserRepository;
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
    private final UserRepository userRepository;

    public Page<Product> getAllProducts(Integer page, Integer numberOfProducts, String sortBy, Boolean isOrderAsc) {
        return productRepository.findAll(
                PageRequest.of(page, numberOfProducts, isOrderAsc ? Sort.Direction.ASC : Sort.Direction.DESC, sortBy));
    }

    public void addProduct(String rawProduct, Long sellerId) {
        Product product = ProductFactory.makeProduct(rawProduct);

        productRepository.save(product);

        User user = userRepository.findById(sellerId)
                .orElseThrow(() -> new UserNotFoundException(Constants.USER_NOT_FOUND_MSG));

        user.addSellingProduct(product);

        userRepository.save(user);
    }
}
