package com.tzsombi.webshop.services;

import com.tzsombi.webshop.models.*;
import com.tzsombi.webshop.repositories.PaymentRepository;
import com.tzsombi.webshop.repositories.ProductRepository;
import com.tzsombi.webshop.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.DirtiesContext;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.result.StatusResultMatchersExtensionsKt.isEqualTo;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ProductServiceTest {

    private ProductService underTestService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @BeforeEach
    void setUp() {
        underTestService = new ProductService(productRepository, userRepository, paymentRepository);
    }

    @Test
    void itShould_getAllProducts() {
        // given
        User user = User.builder()
                .firstName("firstName")
                .lastName("lastName")
                .email("someemail@gmail.com")
                .password("password")
                .role(Role.USER)
                .build();
        User savedUser = userRepository.save(user);
        Phone phone = new Phone(
                "iPhone 6",
                new BigDecimal(100_000),
                "Some description",
                savedUser.getId(), 4,
                "Apple",
                PhoneOperatingSystem.IOS,
                Color.BLACK);
        productRepository.save(phone);
        Computer computer = new Computer(
                "Dell G3 15 3500",
                new BigDecimal(100_000),
                "Some description",
                savedUser.getId(),
                GpuType.MSI_GeForce_RTX_3070,
                CpuType.AMD_RYZEN_6_6980HX,
                16,
                "Dell",
                Color.WHITE,
                ComputerOperatingSystem.WINDOWS);
        productRepository.save(computer);

        // when
        int page = 0;
        int numberOfProducts = 2;
        String sortBy = "name";
        boolean isOrderAsc = true;
        Page<Product> products = underTestService.getAllProducts(page, numberOfProducts, sortBy, isOrderAsc);

        // then
        assertThat(products.getSize()).isEqualTo(numberOfProducts);
        assertThat(products.getTotalPages()).isEqualTo(1);
        assertThat(products.getSort()).isEqualTo(Sort.by(sortBy));
    }

    @Test
    void itShould_updateProduct_ValidPhoneInputDetails() {
        // given
        User user = User.builder()
                .firstName("firstName")
                .lastName("lastName")
                .email("someemail@gmail.com")
                .password("password")
                .role(Role.USER)
                .build();
        User savedUser = userRepository.save(user);
        Phone phone = new Phone(
                "iPhone 6",
                new BigDecimal(100_000),
                "Some description",
                savedUser.getId(),
                4,
                "Apple",
                PhoneOperatingSystem.IOS,
                Color.BLACK);
        Phone savedPhone = productRepository.save(phone);

        String expectedName = "updatedName";
        BigDecimal expectedPrice = new BigDecimal("200.00");
        String expectedDescription = "Updated description";
        Integer expectedRamInGb = 8;
        String expectedManufacturer = "Updated Phone";
        PhoneOperatingSystem expectedPhoneOperatingSystem = PhoneOperatingSystem.ANDROID;
        Color expectedColor = Color.GOLD;

        ProductRequestDTO requestDTO = new ProductRequestDTO(
                expectedName, expectedPrice, expectedDescription,
                expectedRamInGb, expectedManufacturer, expectedPhoneOperatingSystem,
                null, expectedColor, null, null);
        // when
        underTestService.updateProduct(savedPhone.getId(), requestDTO);

        // then
        Phone updatedProduct = (Phone) productRepository.findById(savedPhone.getId())
                .orElse(null);

        assert updatedProduct != null;
        assertThat(updatedProduct.getName()).isEqualTo(expectedName);
        assertThat(updatedProduct.getPrice()).isEqualTo(expectedPrice);
        assertThat(updatedProduct.getDescription()).isEqualTo(expectedDescription);
        assertThat(updatedProduct.getRamInGb()).isEqualTo(expectedRamInGb);
        assertThat(updatedProduct.getManufacturer()).isEqualTo(expectedManufacturer);
        assertThat(updatedProduct.getSystem()).isEqualTo(expectedPhoneOperatingSystem);
        assertThat(updatedProduct.getColor()).isEqualTo(expectedColor);
    }

    @Test
    void itShould_updateProduct_ValidComputerInputDetails() {
        // given
        User user = User.builder()
                .firstName("firstName")
                .lastName("lastName")
                .email("someemail@gmail.com")
                .password("password")
                .role(Role.USER)
                .build();
        User savedUser = userRepository.save(user);
        Computer computer = new Computer(
                "Dell G3 15 3500",
                new BigDecimal(100_000),
                "Some description",
                savedUser.getId(),
                GpuType.MSI_GeForce_RTX_3070,
                CpuType.AMD_RYZEN_6_6980HX,
                16,
                "Dell",
                Color.WHITE,
                ComputerOperatingSystem.WINDOWS);
        Computer savedComputer = productRepository.save(computer);

        String expectedName = "updatedName";
        BigDecimal expectedPrice = new BigDecimal("200.00");
        String expectedDescription = "Updated description";
        Integer expectedRamInGb = 8;
        String expectedManufacturer = "Updated Computer";
        ComputerOperatingSystem expectedComputerOperatingSystem = ComputerOperatingSystem.LINUX;
        Color expectedColor = Color.GOLD;
        GpuType expectedGpuType = GpuType.AMD_Radeon_RX_6950_XT;
        CpuType expectedCpuType = CpuType.AMD_RYZEN_6_6980HX;

        ProductRequestDTO requestDTO = new ProductRequestDTO(
                expectedName, expectedPrice, expectedDescription,
                expectedRamInGb, expectedManufacturer, null,
                expectedComputerOperatingSystem, expectedColor, expectedGpuType, expectedCpuType);

        // when
        underTestService.updateProduct(savedComputer.getId(), requestDTO);

        // then
        Computer updatedProduct = (Computer) productRepository.findById(savedComputer.getId())
                .orElse(null);

        assert updatedProduct != null;
        assertThat(updatedProduct.getName()).isEqualTo(expectedName);
        assertThat(updatedProduct.getPrice()).isEqualTo(expectedPrice);
        assertThat(updatedProduct.getDescription()).isEqualTo(expectedDescription);
        assertThat(updatedProduct.getRamInGb()).isEqualTo(expectedRamInGb);
        assertThat(updatedProduct.getManufacturer()).isEqualTo(expectedManufacturer);
        assertThat(updatedProduct.getSystem()).isEqualTo(expectedComputerOperatingSystem);
        assertThat(updatedProduct.getColor()).isEqualTo(expectedColor);
        assertThat(updatedProduct.getGpu()).isEqualTo(expectedGpuType);
        assertThat(updatedProduct.getCpu()).isEqualTo(expectedCpuType);
    }
}