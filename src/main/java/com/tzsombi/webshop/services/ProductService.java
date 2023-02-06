package com.tzsombi.webshop.services;

import com.tzsombi.webshop.constants.Constants;
import com.tzsombi.webshop.exceptions.AuthException;
import com.tzsombi.webshop.exceptions.ProductNotFoundException;
import com.tzsombi.webshop.exceptions.ProductNotUpdatableException;
import com.tzsombi.webshop.exceptions.UserNotFoundException;
import com.tzsombi.webshop.models.*;
import com.tzsombi.webshop.repositories.ProductRepository;
import com.tzsombi.webshop.repositories.UserRepository;
import com.tzsombi.webshop.utils.CredentialChecker;
import com.tzsombi.webshop.utils.ProductFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

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

        if (product.getSellerId() == null) {
            product.setSellerId(sellerId);
        }

        user.addSellingProduct(product);

        productRepository.save(product);
        userRepository.save(user);
    }

    public void updateProduct(Long productId, ProductRequestDTO productRequestDTO) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(Constants.PRODUCT_NOT_FOUND_MSG));

        String type = productRepository.findProductTypeById(productId);

        Product updatedProduct;
        if (type.equals("Phone")) {
            updatedProduct = updatePhone((Phone) product, productRequestDTO);
        }
        else if (type.equals("Computer")) {
            updatedProduct = updateComputer((Computer) product, productRequestDTO);
        }
        else {
            throw new ProductNotUpdatableException(Constants.PRODUCT_NOT_UPDATABLE_MSG);
        }

        productRepository.save(updatedProduct);
    }

    private Phone updatePhone(Phone phone, ProductRequestDTO productRequestDTO) {
        String name = productRequestDTO.name();
        BigDecimal price = productRequestDTO.price();
        Integer ramInGb = productRequestDTO.ramInGb();
        String manufacturer = productRequestDTO.manufacturer();
        Color color = productRequestDTO.color();
        PhoneOperatingSystem phoneOperatingSystem = productRequestDTO.phoneOperatingSystem();

        if (name != null && name.length() > 0 && ! phone.getName().equals(name)) {
            phone.setName(name);
        }
        if (price != null && ! phone.getPrice().equals(price)) {
            phone.setPrice(price);
        }
        if (ramInGb != null && ramInGb > 0 && ! phone.getRamInGb().equals(ramInGb)) {
            phone.setRamInGb(ramInGb);
        }
        if (manufacturer != null && manufacturer.length() > 0 && ! phone.getManufacturer().equals(manufacturer)) {
            phone.setManufacturer(manufacturer);
        }
        if (color != null && ! phone.getColor().equals(color)) {
            phone.setColor(color);
        }
        if (phoneOperatingSystem != null && ! phone.getSystem().equals(phoneOperatingSystem)) {
            phone.setSystem(phoneOperatingSystem);
        }

        return phone;
    }

    private Computer updateComputer(Computer computer, ProductRequestDTO productRequestDTO) {
        String name = productRequestDTO.name();
        BigDecimal price = productRequestDTO.price();
        Integer ramInGb = productRequestDTO.ramInGb();
        String manufacturer = productRequestDTO.manufacturer();
        Color color = productRequestDTO.color();
        GpuType gpu = productRequestDTO.gpu();
        CpuType cpu = productRequestDTO.cpu();

        if (name != null && name.length() > 0 && ! computer.getName().equals(name)) {
            computer.setName(name);
        }
        if (price != null && ! computer.getPrice().equals(price)) {
            computer.setPrice(price);
        }
        if (ramInGb != null && ramInGb > 0 && ! computer.getRamInGb().equals(ramInGb)) {
            computer.setRamInGb(ramInGb);
        }
        if (manufacturer != null && manufacturer.length() > 0 && ! computer.getManufacturer().equals(manufacturer)) {
            computer.setManufacturer(manufacturer);
        }
        if (color != null && ! computer.getColor().equals(color)) {
            computer.setColor(color);
        }
        if (gpu != null && ! computer.getGpu().equals(gpu)) {
            computer.setGpu(gpu);
        }
        if (cpu != null && ! computer.getCpu().equals(cpu)) {
            computer.setCpu(cpu);
        }

        return computer;
    }

    public void deleteProduct(Long productId, Long sellerId) {
        User user = userRepository.findById(sellerId)
                .orElseThrow(() -> new UserNotFoundException(Constants.USER_NOT_FOUND_MSG));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(Constants.PRODUCT_NOT_FOUND_MSG));

        if (user.getSellingProducts().stream().noneMatch(prod -> prod.getId().equals(productId))) {
            throw new AuthException(Constants.NO_PERMISSION_TO_MODIFY_PRODUCT_MSG);
        }

        user.deleteSellingProduct(product);
        productRepository.delete(product);
        userRepository.save(user);
    }

    public void buyProduct(Long productId, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(Constants.USER_NOT_FOUND_MSG));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(Constants.PRODUCT_NOT_FOUND_MSG));

        CredentialChecker.ifSellerAndBuyerIsTheSameThrowException(product, user);

        product.addBuyer(user);

        productRepository.save(product);
    }
}
