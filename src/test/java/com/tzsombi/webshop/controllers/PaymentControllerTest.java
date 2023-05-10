package com.tzsombi.webshop.controllers;

import com.tzsombi.webshop.AbstractTestContainer;
import com.tzsombi.webshop.auth.JwtService;
import com.tzsombi.webshop.models.CreditCard;
import com.tzsombi.webshop.models.Role;
import com.tzsombi.webshop.models.User;
import com.tzsombi.webshop.repositories.PaymentRepository;
import com.tzsombi.webshop.repositories.ProductRepository;
import com.tzsombi.webshop.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Clock;
import java.time.YearMonth;
import java.time.ZonedDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = FixedClockConfig.class)
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class PaymentControllerTest extends AbstractTestContainer {

    public static final String baseEndpointUrl = "/api/v1/payments";

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
    void itShould_getCard() throws Exception {
        // given
        User user = User.builder()
                .firstName("firstName")
                .lastName("lastName")
                .email("someemail@gmail.com")
                .password("password")
                .role(Role.USER)
                .build();
        User savedUser = userRepository.save(user);

        CreditCard card = new CreditCard(
                "5555555555554444\n",
                YearMonth.from(ZonedDateTime.now(clock)),
                "FirstName LastName",
                true,
                savedUser.getId()
        );
        CreditCard savedCard = paymentRepository.save(card);

        String token = jwtService.generateToken(user);
        // when
        // then
        mockMvc.perform(get(baseEndpointUrl + "/retrieve/{cardId}", savedCard.getId())
                        .header("authorization", "Bearer " + token)
                        .param("userId", savedUser.getId().toString()))
                .andExpect(status().isOk());
    }

    @Test
    void itShould_registerCard() throws Exception {
        // given
        User user = User.builder()
                .firstName("firstName")
                .lastName("lastName")
                .email("someemail@gmail.com")
                .password("password")
                .role(Role.USER)
                .build();
        User savedUser = userRepository.save(user);

        String expiryDate = String.valueOf(YearMonth.from(ZonedDateTime.now(clock)));
        String inputCardAsString = String.format("{" +
                "\"cardNumber\": \"5555555555554444\", " +
                "\"expiryDate\": \"%s\", " +
                "\"fullName\": \"FirstName LastName\", " +
                "\"isActive\": true}", expiryDate);

        String token = jwtService.generateToken(user);
        // when
        // then
        mockMvc.perform(post(baseEndpointUrl + "/register/{userId}", savedUser.getId())
                        .header("authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(inputCardAsString))
                .andExpect(status().isAccepted());
    }

    @Test
    void itShould_deleteCard() throws Exception {
        // given
        User user = User.builder()
                .firstName("firstName")
                .lastName("lastName")
                .email("someemail@gmail.com")
                .password("password")
                .role(Role.USER)
                .build();
        User savedUser = userRepository.save(user);

        CreditCard card = new CreditCard(
                "5555555555554444",
                YearMonth.from(ZonedDateTime.now(clock)),
                "FirstName LastName",
                true,
                savedUser.getId()
        );
        CreditCard savedCard = paymentRepository.save(card);

        String token = jwtService.generateToken(user);
        // when
        // then
        mockMvc.perform(delete(baseEndpointUrl + "/delete/{cardId}", savedCard.getId())
                        .header("authorization", "Bearer " + token)
                        .param("userId", savedUser.getId().toString()))
                .andExpect(status().isOk());
    }

    @Test
    void itShould_updateCard() throws Exception {
        // given
        User user = User.builder()
                .firstName("firstName")
                .lastName("lastName")
                .email("someemail@gmail.com")
                .password("password")
                .role(Role.USER)
                .build();
        User savedUser = userRepository.save(user);

        CreditCard card = new CreditCard(
                "5555555555554444",
                YearMonth.from(ZonedDateTime.now(clock)),
                "FirstName LastName",
                true,
                savedUser.getId()
        );
        CreditCard savedCard = paymentRepository.save(card);

        String expiryDate = String.valueOf(YearMonth.from(ZonedDateTime.now(clock)).plusMonths(1));
        String inputCardAsString = String.format("{" +
                "\"cardNumber\": \"4111111111111111\", " +
                "\"expiryDate\": \"%s\", " +
                "\"fullName\": \"FirstName2 LastName2\", " +
                "\"isActive\": false}", expiryDate);

        String token = jwtService.generateToken(user);
        // when
        // then
        mockMvc.perform(put(baseEndpointUrl + "/update/{cardId}", savedCard.getId())
                        .header("authorization", "Bearer " + token)
                        .param("userId", savedUser.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(inputCardAsString))
                .andExpect(status().isOk());
    }
}