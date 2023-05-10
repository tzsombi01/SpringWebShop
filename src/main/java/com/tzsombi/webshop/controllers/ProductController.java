package com.tzsombi.webshop.controllers;

import com.tzsombi.webshop.models.Product;
import com.tzsombi.webshop.models.ProductRequestDTO;
import com.tzsombi.webshop.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<Page<Product>> getAllProducts(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "5") Integer pageSize,
            @RequestParam(defaultValue = "true") Boolean order,
            @RequestParam(defaultValue = "name") String sortBy
        ) {
        Page<Product> products = productService.getAllProducts(page, pageSize, sortBy, order);
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

    @PutMapping("/update/{productId}")
    public ResponseEntity<String> updateProduct(
            @PathVariable Long productId,
            @RequestBody ProductRequestDTO productRequestDTO) {
        productService.updateProduct(productId, productRequestDTO);
        return new ResponseEntity<>("Product updated successfully!", HttpStatus.OK);
    }

    @DeleteMapping("/delete/{productId}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long productId, @RequestParam Long sellerId) {
        productService.deleteProduct(productId, sellerId);
        return new ResponseEntity<>("Product deleted successfully!", HttpStatus.OK);
    }

    @PostMapping("/buy/{productId}")
    public ResponseEntity<String> buyProduct(@PathVariable Long productId, @RequestParam Long userId) {
        productService.buyProduct(productId, userId);
        return new ResponseEntity<>("Purchase was successful!", HttpStatus.ACCEPTED);
    }
}
