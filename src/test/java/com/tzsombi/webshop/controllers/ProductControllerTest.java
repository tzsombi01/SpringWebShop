package com.tzsombi.webshop.controllers;

import com.tzsombi.webshop.AbstractTestContainer;
import com.tzsombi.webshop.auth.JwtService;
import com.tzsombi.webshop.models.*;
import com.tzsombi.webshop.repositories.PaymentRepository;
import com.tzsombi.webshop.repositories.ProductRepository;
import com.tzsombi.webshop.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestConfiguration
class FixedClockConfig {
    @Primary
    @Bean
    Clock fixedClock() {
        return Clock.fixed(
                Instant.parse("2020-01-01T08:01:01.001Z"),
                ZoneId.of("Europe/Prague"));
    }
}

@SpringBootTest(classes = FixedClockConfig.class)
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ProductControllerTest extends AbstractTestContainer {

    public static final String baseEndpointUrl = "/api/v1/products";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private Clock clock;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        userRepository.deleteAll();
        productRepository.deleteAll();
        paymentRepository.deleteAll();
    }

    @Test
    void itShould_getAllProducts() throws Exception {
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
        Phone savedProduct = productRepository.save(phone);

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
        Computer savedCard = productRepository.save(computer);

        String token = jwtService.generateToken(user);
        // when
        // then
        mockMvc.perform(get(baseEndpointUrl)
                        .header("authorization", "Bearer " + token)
                        .param("page", "0")
                        .param("numberOfProductsPerPage", "2")
                        .param("isOrderAsc", "false")
                        .param("sortBy", "price"))
                .andExpect(status().isOk());
    }

    @Test
    void itShould_addProduct() throws Exception {
        // given
        User user = User.builder()
                .firstName("firstName")
                .lastName("lastName")
                .email("someemail@gmail.com")
                .password("password")
                .role(Role.USER)
                .build();
        User savedUser = userRepository.save(user);

        String rawProductInput = String.format("{" +
                "\"type\": \"Phone\", " +
                "\"name\": \"Galaxy A5\", " +
                "\"price\": \"500.50\", " +
                "\"description\": \"Some description\", " +
                "\"sellerId\": %s, " +
                "\"ramInGb\": 4, " +
                "\"manufacturer\": \"Asus\", " +
                "\"system\": \"ios\"," +
                "\"color\": \"gold\"" +
                "}", savedUser.getId());
        String token = jwtService.generateToken(user);
        // when
        // then
        mockMvc.perform(post(baseEndpointUrl + "/add/{sellerId}", savedUser.getId())
                        .header("authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(rawProductInput))
                .andExpect(status().isCreated());
    }

    @Test
    void itShould_updateProduct() throws Exception {
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

        String requestDTOasString = "{" +
                "\"name\": \"Galaxy A6\", " +
                "\"price\": 150.05, " +
                "\"system\": \"ANDROID\", " +
                "\"color\": \"BLACK\"" +
                "}";
        String token = jwtService.generateToken(user);
        // when
        // then
        mockMvc.perform(put(baseEndpointUrl + "/update/{productId}", savedComputer.getId())
                        .header("authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestDTOasString))
                .andExpect(status().isOk());
    }

    @Test
    void itShould_buyProduct() throws Exception {
        // given
        User user1 = User.builder()
                .firstName("firstName")
                .lastName("lastName")
                .email("someemail@gmail.com")
                .password("password")
                .role(Role.USER)
                .build();
        User savedUser1 = userRepository.save(user1);

        User user2 = User.builder()
                .firstName("firstName")
                .lastName("lastName")
                .email("someemail2@gmail.com")
                .password("password")
                .role(Role.USER)
                .build();
        User savedUser2 = userRepository.save(user2);

        CreditCard card = new CreditCard(
                "1234-5678-1234-5678",
                YearMonth.from(ZonedDateTime.now(clock)),
                "FirstName LastName",
                true,
                savedUser2.getId()
        );
        CreditCard savedCard = paymentRepository.save(card);

        Computer computer = new Computer(
                "Dell G3 15 3500",
                new BigDecimal(100_000),
                "Some description",
                savedUser1.getId(),
                GpuType.MSI_GeForce_RTX_3070,
                CpuType.AMD_RYZEN_6_6980HX,
                16,
                "Dell",
                Color.WHITE,
                ComputerOperatingSystem.WINDOWS);
        Computer savedComputer = productRepository.save(computer);

        String token = jwtService.generateToken(user2);
        // when
        // then
        mockMvc.perform(post(baseEndpointUrl + "/buy/{productId}", savedComputer.getId())
                        .header("authorization", "Bearer " + token)
                        .param("userId", savedUser2.getId().toString()))
                .andExpect(status().isAccepted());
    }

    @Test
    void itShould_deleteProduct() throws Exception {
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

        String token = jwtService.generateToken(user);
        // when
        // then
        mockMvc.perform(delete(baseEndpointUrl + "/delete/{productId}", savedComputer.getId())
                        .header("authorization", "Bearer " + token)
                        .param("sellerId", savedUser.getId().toString()))
                .andExpect(status().isOk());
    }
}