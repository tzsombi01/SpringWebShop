package com.tzsombi.webshop.services;

import com.tzsombi.webshop.constants.ErrorConstants;
import com.tzsombi.webshop.exceptions.*;
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
import java.time.Clock;
import java.time.YearMonth;
import java.time.ZonedDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@SpringBootTest(classes = FixedClockConfig.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ProductServiceTest {

    private ProductService underTestService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private Clock clock;

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
        CpuType expectedCpuType = CpuType.AMD_RYZEN_5_2600;

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

    @Test
    void itShould_addProduct_ValidInput() {
        // given
        User user = User.builder()
                .firstName("firstName")
                .lastName("lastName")
                .email("someemail@gmail.com")
                .password("password")
                .role(Role.USER)
                .build();
        User savedUser = userRepository.save(user);

        String rawProductInput = """
                {
                    "type": "Phone",
                    "name": "Galaxy A5",
                    "price": "500.50",
                    "description": "Some description",
                    "sellerId": 1,
                    "ramInGb": 4,
                    "manufacturer": "Asus",
                    "system": "ios",
                    "color": "gold"
                }
                """;
        // when
        underTestService.addProduct(rawProductInput, savedUser.getId());
        String expectedType = "Phone";
        Product savedProduct = productRepository.findById(1L)
                .orElse(null);

        String type = productRepository.findProductTypeById(1L);

        User userAfter = userRepository.findById(savedUser.getId())
                .orElse(null);

        // then
        assert userAfter != null;
        assertThat(savedProduct).isNotNull();
        assertThat(type).isEqualTo(expectedType);
        assertThat(userAfter.getSellingProducts().size()).isEqualTo(1);
    }

    @Test
    void itShouldThrow_addProduct_UserDoesNotExists() {
        // given
        String rawProductInput = """
                {
                    "type": "Phone",
                    "name": "Galaxy A5",
                    "price": "500.50",
                    "description": "Some description",
                    "sellerId": 1,
                    "ramInGb": 4,
                    "manufacturer": "Asus",
                    "system": "ios",
                    "color": "gold"
                }
                """;

        // when
        // then
        assertThatThrownBy(() -> underTestService.addProduct(rawProductInput, 1L))
                .isInstanceOf(RuntimeException.class)
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining("Reason: 'USER_NOT_FOUND' Id: '1'");
    }

    @Test
    void itShould_addProduct_ToUsersListOfProducts() {
        // given
        User user = User.builder()
                .firstName("firstName")
                .lastName("lastName")
                .email("someemail@gmail.com")
                .password("password")
                .role(Role.USER)
                .build();
        User savedUser = userRepository.save(user);

        String rawProductInput = """
                {
                    "type": "Phone",
                    "name": "Galaxy A5",
                    "price": "500.50",
                    "description": "Some description",
                    "sellerId": 1,
                    "ramInGb": 4,
                    "manufacturer": "Asus",
                    "system": "ios",
                    "color": "gold"
                }
                """;
        // when
        underTestService.addProduct(rawProductInput, savedUser.getId());

        Product savedProduct = productRepository.findById(1L)
                .orElse(null);

        User userAfter = userRepository.findById(savedUser.getId())
                .orElse(null);
        // then
        assert savedProduct != null;
        assert userAfter != null;
        assertThat(userAfter.getSellingProducts().size()).isEqualTo(1);
        assertThat(savedProduct.getSellerId()).isEqualTo(savedUser.getId());
    }

    @Test
    void itShould_deleteProduct_ValidProdId() {
        // given
        User user = User.builder()
                .firstName("firstName")
                .lastName("lastName")
                .email("someemail@gmail.com")
                .password("password")
                .role(Role.USER)
                .build();
        userRepository.save(user);
        Computer computer = new Computer(
                "Dell G3 15 3500",
                new BigDecimal(100_000),
                "Some description",
                1L,
                GpuType.MSI_GeForce_RTX_3070,
                CpuType.AMD_RYZEN_6_6980HX,
                16,
                "Dell",
                Color.WHITE,
                ComputerOperatingSystem.WINDOWS);
        productRepository.save(computer);
        // when
        underTestService.deleteProduct(1L, 1L);

        User userAfter = userRepository.findById(1L)
                .orElse(null);

        Product productAfter = productRepository.findById(1L)
                .orElse(null);
        // then
        assert userAfter != null;
        assertThat(productAfter).isNull();
        assertThat(userAfter.getSellingProducts().size()).isEqualTo(0);
    }

    @Test
    void itShouldThrowUserNotFound_deleteProduct_UserDoesNotExist() {
        // given
        // when
        // then
        assertThatThrownBy(() -> underTestService.deleteProduct(1L, 1L))
                .isInstanceOf(RuntimeException.class)
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining("Reason: 'USER_NOT_FOUND' Id: '1'");
    }

    @Test
    void itShouldThrowProductNotFound_deleteProduct_ProductDoesNotExist() {
        // given
        User user = User.builder()
                .firstName("firstName")
                .lastName("lastName")
                .email("someemail@gmail.com")
                .password("password")
                .role(Role.USER)
                .build();
        userRepository.save(user);
        // when
        // then
        assertThatThrownBy(() -> underTestService.deleteProduct(1L, 1L))
                .isInstanceOf(RuntimeException.class)
                .isInstanceOf(ProductNotFoundException.class)
                .hasMessageContaining(ErrorConstants.PRODUCT_NOT_FOUND_MSG);
    }

    @Test
    void itShouldThrowAuthException_deleteProduct_BothExistButNotUsersProduct() {
        // given
        User user1 = User.builder()
                .firstName("firstName")
                .lastName("lastName")
                .email("someemail@gmail.com")
                .password("password")
                .role(Role.USER)
                .build();
        userRepository.save(user1);

        User user2 = User.builder()
                .firstName("firstName")
                .lastName("lastName")
                .email("someemail2@gmail.com")
                .password("password")
                .role(Role.USER)
                .build();
        userRepository.save(user2);

        Computer computer = new Computer(
                "Dell G3 15 3500",
                new BigDecimal(100_000),
                "Some description",
                1L,
                GpuType.MSI_GeForce_RTX_3070,
                CpuType.AMD_RYZEN_6_6980HX,
                16,
                "Dell",
                Color.WHITE,
                ComputerOperatingSystem.WINDOWS);
        productRepository.save(computer);
        // when
        // then
        assertThatThrownBy(() -> underTestService.deleteProduct(1L, 2L))
                .isInstanceOf(RuntimeException.class)
                .isInstanceOf(AuthException.class)
                .hasMessageContaining(ErrorConstants.NO_PERMISSION_TO_MODIFY_PRODUCT_MSG);
    }

    @Test
    void itShould_buyProduct() {
        // given
        User user1 = User.builder()
                .firstName("firstName")
                .lastName("lastName")
                .email("someemail@gmail.com")
                .password("password")
                .role(Role.USER)
                .build();
        userRepository.save(user1);
        User user2 = User.builder()
                .firstName("firstName")
                .lastName("lastName")
                .email("someemail2@gmail.com")
                .password("password")
                .role(Role.USER)
                .build();
        userRepository.save(user2);
        Computer computer = new Computer(
                "Dell G3 15 3500",
                new BigDecimal(100_000),
                "Some description",
                1L,
                GpuType.MSI_GeForce_RTX_3070,
                CpuType.AMD_RYZEN_6_6980HX,
                16,
                "Dell",
                Color.WHITE,
                ComputerOperatingSystem.WINDOWS);
        productRepository.save(computer);
        CreditCard card = new CreditCard(
                "1234-5678-1234-5678",
                YearMonth.from(ZonedDateTime.now(clock)),
                "FirstName LastName",
                 true,
                2L
        );
        paymentRepository.save(card);
        // when
        underTestService.buyProduct(1L, 2L);

        Product productAfter = productRepository.findById(1L)
                .orElse(null);
        // then
        assert productAfter != null;
        assertThat(productAfter.getBuyers().size()).isEqualTo(1);
    }

    @Test
    void itShouldThrow_buyProduct_NoActiveCard() {
        // given
        User user1 = User.builder()
                .firstName("firstName")
                .lastName("lastName")
                .email("someemail@gmail.com")
                .password("password")
                .role(Role.USER)
                .build();
        userRepository.save(user1);
        User user2 = User.builder()
                .firstName("firstName")
                .lastName("lastName")
                .email("someemail2@gmail.com")
                .password("password")
                .role(Role.USER)
                .build();
        userRepository.save(user2);
        Computer computer = new Computer(
                "Dell G3 15 3500",
                new BigDecimal(100_000),
                "Some description",
                1L,
                GpuType.MSI_GeForce_RTX_3070,
                CpuType.AMD_RYZEN_6_6980HX,
                16,
                "Dell",
                Color.WHITE,
                ComputerOperatingSystem.WINDOWS);
        productRepository.save(computer);

        // when
        // then
        assertThatThrownBy(() -> underTestService.buyProduct(1L, 2L))
                .isInstanceOf(RuntimeException.class)
                .isInstanceOf(NoActiveCardFoundException.class)
                .hasMessageContaining(ErrorConstants.SET_AN_ACTIVE_CARD_MSG);
    }

    @Test
    void itShouldThrow_buyProduct_ProductDoesNotExist() {
        // given
        User user1 = User.builder()
                .firstName("firstName")
                .lastName("lastName")
                .email("someemail@gmail.com")
                .password("password")
                .role(Role.USER)
                .build();
        userRepository.save(user1);
        // when
        // then
        assertThatThrownBy(() -> underTestService.buyProduct(1L, 1L))
                .isInstanceOf(RuntimeException.class)
                .isInstanceOf(ProductNotFoundException.class)
                .hasMessageContaining(ErrorConstants.PRODUCT_NOT_FOUND_MSG);
    }

    @Test
    void itShouldThrow_buyProduct_UserDoesNotExist() {
        // given
        // when
        // then
        assertThatThrownBy(() -> underTestService.buyProduct(1L, 1L))
                .isInstanceOf(RuntimeException.class)
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining("Reason: 'USER_NOT_FOUND' Id: '1'");
    }

    @Test
    void itShouldThrow_buyProduct_CanNotBuyOwnProduct() {
        // given
        User user = User.builder()
                .firstName("firstName")
                .lastName("lastName")
                .email("someemail@gmail.com")
                .password("password")
                .role(Role.USER)
                .build();
        userRepository.save(user);
        Computer computer = new Computer(
                "Dell G3 15 3500",
                new BigDecimal(100_000),
                "Some description",
                1L,
                GpuType.MSI_GeForce_RTX_3070,
                CpuType.AMD_RYZEN_6_6980HX,
                16,
                "Dell",
                Color.WHITE,
                ComputerOperatingSystem.WINDOWS);
        productRepository.save(computer);

        // when
        // then
        assertThatThrownBy(() -> underTestService.buyProduct(1L, 1L))
                .isInstanceOf(RuntimeException.class)
                .isInstanceOf(CanNotBuyOwnProductExeption.class)
                .hasMessageContaining(ErrorConstants.CANNOT_BUY_YOUR_OWN_PRODUCT_MSG);
    }
}