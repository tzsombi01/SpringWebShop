package com.tzsombi.webshop.repositories;

import com.tzsombi.webshop.models.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;

import java.math.BigDecimal;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ProductRepositoryTest {

    @Autowired
    private ProductRepository underTestRepository;

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    void itShould_findProductTypeById_ExistingProduct() {
        // given
        String expected = "Phone";
        User user = User.builder()
                .firstName("firstName")
                .lastName("lastName")
                .email("someemail@gmail.com")
                .password("password")
                .role(Role.USER)
                .build();

        User savedUser = userRepository.save(user);
        Phone phone = new Phone(
                "phoneName",
                BigDecimal.valueOf(100.10),
                "description",
                savedUser.getId(),
                4,
                "Apple",
                PhoneOperatingSystem.IOS,
                Color.BLACK
        );
        Phone savedPhone = underTestRepository.save(phone);

        // when
        String type = underTestRepository.findProductTypeById(savedPhone.getId());
        // then
        assertThat(type).isEqualTo(expected);
    }

    @Test
    void itShouldNot_findProductTypeById_NotExistingProduct() {
        // given
        User user = User.builder()
                .firstName("firstName")
                .lastName("lastName")
                .email("someemail@gmail.com")
                .password("password")
                .role(Role.USER)
                .build();

        User savedUser = userRepository.save(user);

        // when
        String type = underTestRepository.findProductTypeById(1L);

        // then
        assertThat(type).isEqualTo(null);
    }
}