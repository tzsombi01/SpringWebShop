package com.tzsombi.webshop.services;

import com.tzsombi.webshop.constants.ErrorConstants;
import com.tzsombi.webshop.exceptions.AuthException;
import com.tzsombi.webshop.exceptions.CardNotFoundException;
import com.tzsombi.webshop.exceptions.ExpiryDateMustBeAfterCurrentDateException;
import com.tzsombi.webshop.exceptions.UserNotFoundException;
import com.tzsombi.webshop.models.*;
import com.tzsombi.webshop.repositories.PaymentRepository;
import com.tzsombi.webshop.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.test.annotation.DirtiesContext;

import java.time.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

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
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class PaymentServiceTest {

    private PaymentService underTestService;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private CreditCardResponseDTOMapper creditCardResponseDTOMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private Clock clock;

    @BeforeEach
    void setUp() {
        underTestService = new PaymentService(paymentRepository, creditCardResponseDTOMapper, userRepository, clock);
    }

    @Test
    void itShould_getCard() {
        // given
        User user = User.builder()
                .firstName("firstName")
                .lastName("lastName")
                .email("someemail@gmail.com")
                .password("password")
                .role(Role.USER)
                .build();
        userRepository.save(user);
        CreditCard expected = new CreditCard(
                "1234-5678-1234-5678",
                YearMonth.from(ZonedDateTime.now(clock)),
                "FirstName LastName",
                true,
                1L
        );
        paymentRepository.save(expected);

        // when
        CreditCardResponseDTO responseDTO = underTestService.getCard(1L, 1L);

        // then
        assertThat(responseDTO.cardNumber()).isEqualTo(expected.getCardNumber());
        assertThat(responseDTO.expiryDate()).isEqualTo(expected.getExpiryDate());
        assertThat(responseDTO.fullName()).isEqualTo(expected.getFullName());
        assertThat(responseDTO.isActive()).isEqualTo(expected.getIsActive());
    }

    @Test
    void itShouldThrow_getCard_UserNotFound() {
        // given
        User user = User.builder()
                .firstName("firstName")
                .lastName("lastName")
                .email("someemail@gmail.com")
                .password("password")
                .role(Role.USER)
                .build();
        userRepository.save(user);
        CreditCard expected = new CreditCard(
                "1234-5678-1234-5678",
                YearMonth.from(ZonedDateTime.now(clock)),
                "FirstName LastName",
                true,
                1L
        );
        paymentRepository.save(expected);

        // when
        // then
        assertThatThrownBy(() -> underTestService.getCard(1L, 2L))
                .isInstanceOf(RuntimeException.class)
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining("Reason: 'USER_NOT_FOUND' Id: '2'");

    }

    @Test
    void itShouldThrow_getCard_CardNotFound() {
        // given
        User user = User.builder()
                .firstName("firstName")
                .lastName("lastName")
                .email("someemail@gmail.com")
                .password("password")
                .role(Role.USER)
                .build();
        userRepository.save(user);
        CreditCard expected = new CreditCard(
                "1234-5678-1234-5678",
                YearMonth.from(ZonedDateTime.now(clock)),
                "FirstName LastName",
                true,
                1L
        );
        paymentRepository.save(expected);

        // when
        // then
        assertThatThrownBy(() -> underTestService.getCard(2L, 1L))
                .isInstanceOf(RuntimeException.class)
                .isInstanceOf(CardNotFoundException.class)
                .hasMessageContaining(ErrorConstants.CARD_NOT_FOUND_MSG);
    }

    @Test
    void itShouldThrow_getCard_NoPermissionToAccessCard() {
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

        CreditCard expected = new CreditCard(
                "1234-5678-1234-5678",
                YearMonth.from(ZonedDateTime.now(clock)),
                "FirstName LastName",
                true,
                1L
        );
        paymentRepository.save(expected);

        // when
        // then
        assertThatThrownBy(() -> underTestService.getCard(1L, 2L))
                .isInstanceOf(RuntimeException.class)
                .isInstanceOf(AuthException.class)
                .hasMessageContaining(ErrorConstants.NO_PERMISSION_TO_MODIFY_CARD);
    }


    @Test
    void itShould_registerCard_ValidInfo() {
        // given
        User user = User.builder()
                .firstName("firstName")
                .lastName("lastName")
                .email("someemail@gmail.com")
                .password("password")
                .role(Role.USER)
                .build();
        userRepository.save(user);
        CreditCardRequestDTO requestDTO = new CreditCardRequestDTO(
                "5555555555554444",
                YearMonth.from(ZonedDateTime.now(clock)),
                true,
                "FirstName LastName"
        );

        // when
        underTestService.register(requestDTO, 1L);

        User userAfter = userRepository.findById(1L)
                .orElse(null);

        CreditCard expected = paymentRepository.findById(1L)
                .orElse(null);
        // then
        assert userAfter != null;
        assertThat(userAfter.getCards().size()).isEqualTo(1);
        assertThat(expected).isNotNull();
    }

    @Test
    void itShouldThrow_registerCard_InValidInfo() {
        // given
        User user = User.builder()
                .firstName("firstName")
                .lastName("lastName")
                .email("someemail@gmail.com")
                .password("password")
                .role(Role.USER)
                .build();
        userRepository.save(user);
        CreditCardRequestDTO requestDTO = new CreditCardRequestDTO(
                "5555555555554444",
                YearMonth.from(ZonedDateTime.now(clock).minusMonths(1)),
                true,
                "FirstName LastName"
        );

        // when
        // then
        assertThatThrownBy(() -> underTestService.register(requestDTO, 1L))
                .isInstanceOf(RuntimeException.class)
                .isInstanceOf(ExpiryDateMustBeAfterCurrentDateException.class)
                .hasMessageContaining(ErrorConstants.EXPIRY_MUST_BE_AFTER_CURRENT_DATE_MSG);
    }

    @Test
    void itShould_registerCard_MakeActiveCardNotActiveBefore() {
        // given
        User user = User.builder()
                .firstName("firstName")
                .lastName("lastName")
                .email("someemail@gmail.com")
                .password("password")
                .role(Role.USER)
                .build();
        userRepository.save(user);
        CreditCard creditCard = new CreditCard(
                "5555555555554444",
                YearMonth.from(ZonedDateTime.now(clock)),
                "FirstName LastName",
                true,
                1L
        );
        paymentRepository.save(creditCard);
        CreditCardRequestDTO requestDTO = new CreditCardRequestDTO(
                "5555555555554444",
                YearMonth.from(ZonedDateTime.now(clock)),
                true,
                "FirstName LastName"
        );

        // when
        underTestService.register(requestDTO, 1L);

        CreditCard firstCardAfter = paymentRepository.findById(1L)
                .orElse(null);

        User userAfter = userRepository.findById(1L)
                .orElse(null);
        // then
        assert firstCardAfter != null;
        assert userAfter != null;
        assertThat(firstCardAfter.getIsActive()).isFalse();
        assertThat(userAfter.getCards().size()).isEqualTo(2);
    }

    @Test
    void itShould_deleteCard() {
        // given
        User user = User.builder()
                .firstName("firstName")
                .lastName("lastName")
                .email("someemail@gmail.com")
                .password("password")
                .role(Role.USER)
                .build();
        userRepository.save(user);
        CreditCard creditCard = new CreditCard(
                "5555555555554444",
                YearMonth.from(ZonedDateTime.now(clock)),
                "FirstName LastName",
                true,
                1L
        );
        paymentRepository.save(creditCard);

        // when
        underTestService.deleteCard(1L, 1L);

        CreditCard cardAfter = paymentRepository.findById(1L)
                .orElse(null);

        User userAfter = userRepository.findById(1L)
                .orElse(null);
        // then
        assert userAfter != null;
        assertThat(cardAfter).isNull();
        assertThat(userAfter.getCards().size()).isEqualTo(0);
    }

    @Test
    void itShouldThrow_deleteCard_UserNotFound() {
        // given
        // when
        // then
        assertThatThrownBy(() -> underTestService.deleteCard(1L, 1L))
                .isInstanceOf(RuntimeException.class)
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining("Reason: 'USER_NOT_FOUND' Id: '1'");
    }

    @Test
    void itShouldThrow_deleteCard_CardNotFound() {
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
        assertThatThrownBy(() -> underTestService.deleteCard(1L, 1L))
                .isInstanceOf(RuntimeException.class)
                .isInstanceOf(CardNotFoundException.class)
                .hasMessageContaining(ErrorConstants.CARD_NOT_FOUND_MSG);
    }

    @Test
    void itShouldThrow_deleteCard_CardIsNotUnderUser() {
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
        CreditCard creditCard = new CreditCard(
                "5555555555554444",
                YearMonth.from(ZonedDateTime.now(clock)),
                "FirstName LastName",
                true,
                1L
        );
        paymentRepository.save(creditCard);
        // when
        // then
        assertThatThrownBy(() -> underTestService.deleteCard(1L, 2L))
                .isInstanceOf(RuntimeException.class)
                .isInstanceOf(AuthException.class)
                .hasMessageContaining(ErrorConstants.NO_PERMISSION_TO_MODIFY_CARD);
    }

    @Test
    void itShould_updateCard() {
        // given
        User user = User.builder()
                .firstName("firstName")
                .lastName("lastName")
                .email("someemail@gmail.com")
                .password("password")
                .role(Role.USER)
                .build();
        userRepository.save(user);
        CreditCard creditCard = new CreditCard(
                "5555555555554444",
                YearMonth.from(ZonedDateTime.now(clock)),
                "FirstName LastName",
                true,
                1L
        );
        paymentRepository.save(creditCard);
        String expectedCardNumber = "4111111111111111";
        YearMonth expectedExpiryDate = YearMonth.from(ZonedDateTime.now(clock)).plusMonths(1);
        boolean expectedIsActive= true;
        String expectedFullName = "Another Name";
        CreditCardRequestDTO requestDTO = new CreditCardRequestDTO(
                expectedCardNumber,
                expectedExpiryDate,
                expectedIsActive,
                expectedFullName
        );
        // when
        underTestService.updateCard(requestDTO, 1L, 1L);

        CreditCard cardAfter = paymentRepository.findById(1L)
                .orElse(null);
        // then
        assert cardAfter != null;
        assertThat(cardAfter.getCardNumber()).isEqualTo(expectedCardNumber);
        assertThat(cardAfter.getExpiryDate()).isEqualTo(expectedExpiryDate);
        assertThat(cardAfter.getIsActive()).isEqualTo(expectedIsActive);
        assertThat(cardAfter.getFullName()).isEqualTo(expectedFullName);
    }

    @Test
    void itShouldThrow_updateCard_UserDoesNotExist() {
        // given
        CreditCardRequestDTO requestDTO = new CreditCardRequestDTO(
                "5555555555554444",
                YearMonth.from(ZonedDateTime.now(clock)),
                true,
                "FirstName LastName"
        );
        // when
        // then
        assertThatThrownBy(() -> underTestService.updateCard(requestDTO, 1L, 1L))
                .isInstanceOf(RuntimeException.class)
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining("Reason: 'USER_NOT_FOUND' Id: '1'");
    }

    @Test
    void itShouldThrow_updateCard_CardDoesNotExist() {
        // given
        User user = User.builder()
                .firstName("firstName")
                .lastName("lastName")
                .email("someemail@gmail.com")
                .password("password")
                .role(Role.USER)
                .build();
        userRepository.save(user);
        CreditCardRequestDTO requestDTO = new CreditCardRequestDTO(
                "5555555555554444",
                YearMonth.from(ZonedDateTime.now(clock)),
                true,
                "FirstName LastName"
        );
        // when
        // then
        assertThatThrownBy(() -> underTestService.updateCard(requestDTO, 1L, 1L))
                .isInstanceOf(RuntimeException.class)
                .isInstanceOf(CardNotFoundException.class)
                .hasMessageContaining(ErrorConstants.CARD_NOT_FOUND_MSG);
    }

    @Test
    void itShouldThrow_updateCard_CardIsNotUnderUser() {
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
        CreditCard creditCard = new CreditCard(
                "5555555555554444",
                YearMonth.from(ZonedDateTime.now(clock)),
                "FirstName LastName",
                true,
                1L
        );
        paymentRepository.save(creditCard);
        CreditCardRequestDTO requestDTO = new CreditCardRequestDTO(
                "5555555555554444",
                YearMonth.from(ZonedDateTime.now(clock)),
                true,
                "FirstName LastName"
        );
        // when
        // then
        assertThatThrownBy(() -> underTestService.updateCard(requestDTO, 1L, 2L))
                .isInstanceOf(RuntimeException.class)
                .isInstanceOf(AuthException.class)
                .hasMessageContaining(ErrorConstants.NO_PERMISSION_TO_MODIFY_CARD);
    }

    @Test
    void itShould_updateCard_PrevActiveCardSetToFalse() {
        // given
        User user = User.builder()
                .firstName("firstName")
                .lastName("lastName")
                .email("someemail@gmail.com")
                .password("password")
                .role(Role.USER)
                .build();
        userRepository.save(user);
        CreditCard creditCard1 = new CreditCard(
                "5555555555554444",
                YearMonth.from(ZonedDateTime.now(clock)),
                "FirstName LastName",
                true,
                1L
        );
        paymentRepository.save(creditCard1);
        CreditCard creditCard2 = new CreditCard(
                "5555555555554444",
                YearMonth.from(ZonedDateTime.now(clock)),
                "FirstName LastName",
                false,
                1L
        );
        paymentRepository.save(creditCard2);
        CreditCardRequestDTO requestDTO = new CreditCardRequestDTO(
                null,
                null,
                true,
                null
        );
        // when
        underTestService.updateCard(requestDTO, 2L, 1L);

        CreditCard card1After = paymentRepository.findById(1L)
                .orElse(null);

        CreditCard card2After = paymentRepository.findById(2L)
                .orElse(null);
        // then
        assert card1After != null;
        assert card2After != null;
        assertThat(card1After.getIsActive()).isFalse();
        assertThat(card2After.getIsActive()).isTrue();
    }
}