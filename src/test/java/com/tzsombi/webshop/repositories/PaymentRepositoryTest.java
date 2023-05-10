package com.tzsombi.webshop.repositories;

import com.tzsombi.webshop.AbstractTestContainer;
import com.tzsombi.webshop.models.CreditCard;
import com.tzsombi.webshop.models.Role;
import com.tzsombi.webshop.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.YearMonth;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class PaymentRepositoryTest extends AbstractTestContainer {

    @Autowired
    private PaymentRepository underTestRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setup() {
        userRepository.deleteAll();
        underTestRepository.deleteAll();
    }

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
        CreditCard foundCard = underTestRepository.findActiveCardUnderUserById(savedUser.getId())
                .orElse(null);
        // then
        assertThat(foundCard).isNull();
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
        assertThat(foundCard).isNull();
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