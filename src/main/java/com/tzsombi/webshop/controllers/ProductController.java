package com.tzsombi.webshop.controllers;

import com.tzsombi.webshop.models.Product;
import com.tzsombi.webshop.services.ProductService;
import lombok.RequiredArgsConstructor;
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
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<String> addProduct(
            @RequestBody String rawProduct
    ) {
        productService.addProduct(rawProduct);
        return new ResponseEntity<>("Product added successfully!", HttpStatus.CREATED);
    }
}
