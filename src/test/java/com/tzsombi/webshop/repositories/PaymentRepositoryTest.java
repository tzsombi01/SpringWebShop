package com.tzsombi.webshop.repositories;

import com.tzsombi.webshop.models.CreditCard;
import com.tzsombi.webshop.models.Role;
import com.tzsombi.webshop.models.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;

import java.time.YearMonth;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class PaymentRepositoryTest {

    @Autowired
    private PaymentRepository underTestRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void itShould_findActiveCardUnderUserById_ExistingUserAndCard() {
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
                "1234-5678-1234-5678",
                YearMonth.of(2023, 3),
                "Full Name",
                true,
                savedUser.getId()
        );
        CreditCard expected = underTestRepository.save(card);

        // when
        CreditCard foundCard = underTestRepository.findActiveCardUnderUserById(user.getId())
                .orElse(null);

        // then
        assertThat(foundCard).isEqualTo(expected);
    }

    @Test
    void itShouldNot_findActiveCardUnderUserById_ExistingUserAndNotExistingCard() {
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
        CreditCard foundCard = underTestRepository.findActiveCardUnderUserById(1L)
                .orElse(null);
        // then
        assertThat(foundCard).isEqualTo(null);
    }

    @Test
    void itShouldNot_findActiveCardUnderUserById_ExistingUserAndExistingCardButInactive() {
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
                "1234-5678-1234-5678",
                YearMonth.of(2023, 3),
                "Full Name",
                false,
                savedUser.getId()
        );
        underTestRepository.save(card);

        // when
        CreditCard foundCard = underTestRepository.findActiveCardUnderUserById(user.getId())
                .orElse(null);

        // then
        assertThat(foundCard).isEqualTo(null);
    }

    @Test
    void itShould_findActiveCardUnderUserById_ExistingUserAndExistingCardOneActiveOneInactive() {
        // given
        User user = User.builder()
                .firstName("firstName")
                .lastName("lastName")
                .email("someemail@gmail.com")
                .password("password")
                .role(Role.USER)
                .build();

        User savedUser = userRepository.save(user);
        CreditCard card1 = new CreditCard(
                "1234-5678-1234-5678",
                YearMonth.of(2023, 3),
                "Full Name",
                false,
                savedUser.getId()
        );
        CreditCard expectedCard = new CreditCard(
                "1234-5678-1234-5678",
                YearMonth.of(2023, 3),
                "Full Name",
                true,
                savedUser.getId()
        );
        underTestRepository.save(card1);
        underTestRepository.save(expectedCard);

        // when
        CreditCard foundCard = underTestRepository.findActiveCardUnderUserById(user.getId())
                .orElse(null);

        // then
        assertThat(foundCard).isEqualTo(expectedCard);
    }
}