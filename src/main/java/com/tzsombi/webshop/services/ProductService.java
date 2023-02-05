package com.tzsombi.webshop.services;

import com.tzsombi.webshop.constants.Constants;
import com.tzsombi.webshop.exceptions.AuthException;
import com.tzsombi.webshop.exceptions.ProductNotFoundException;
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
        User user = userRepository.findById(sellerId)
                .orElseThrow(() -> new UserNotFoundException(Constants.USER_NOT_FOUND_MSG));

        Product product = ProductFactory.makeProduct(rawProduct);

        if(product.getSellerId() == null) {
            product.setSellerId(sellerId);
        }

        user.addSellingProduct(product);

        productRepository.save(product);
        userRepository.save(user);
    }

    public void deleteProduct(Long productId, Long sellerId) {
        User user = userRepository.findById(sellerId)
                .orElseThrow(() -> new UserNotFoundException(Constants.USER_NOT_FOUND_MSG));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(Constants.PRODUCT_NOT_FOUND));

        if (user.getSellingProducts().stream().noneMatch(prod -> prod.getId().equals(productId))) {
            throw new AuthException(Constants.NO_PERMISSION_TO_MODIFY_PRODUCT_MSG);
        }

        user.deleteSellingProduct(product);
        productRepository.delete(product);
        userRepository.save(user);
    }
}
