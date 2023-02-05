package com.tzsombi.webshop.controllers;

import com.tzsombi.webshop.models.Product;
import com.tzsombi.webshop.models.ProductResponseDTO;
import com.tzsombi.webshop.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<Page<Product>> getAllProducts(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer numberOfProductsPerPage,
            @RequestParam(defaultValue = "true") Boolean isOrderAsc,
            @RequestParam(defaultValue = "name") String sortBy
            ) {
        Page<Product> products = productService.getAllProducts(page, numberOfProductsPerPage, sortBy, isOrderAsc);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @PostMapping("/add/{sellerId}")
    public ResponseEntity<String> addProduct(
            @PathVariable Long sellerId,
            @RequestBody String rawProduct
    ) {
        productService.addProduct(rawProduct, sellerId);
        return new ResponseEntity<>("Product added successfully!", HttpStatus.CREATED);
    }
}