package com.tzsombi.webshop.services;

import com.tzsombi.webshop.constants.ErrorConstants;
import com.tzsombi.webshop.error_handling.ErrorCode;
import com.tzsombi.webshop.exceptions.CardNotFoundException;
import com.tzsombi.webshop.exceptions.StateMisMatchException;
import com.tzsombi.webshop.exceptions.UserNotFoundException;
import com.tzsombi.webshop.models.CreditCard;
import com.tzsombi.webshop.models.CreditCardRequestDTO;
import com.tzsombi.webshop.models.CreditCardResponseDTO;
import com.tzsombi.webshop.models.User;
import com.tzsombi.webshop.repositories.PaymentRepository;
import com.tzsombi.webshop.repositories.UserRepository;
import com.tzsombi.webshop.utils.CredentialChecker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.YearMonth;
import java.time.ZonedDateTime;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;

    private final CreditCardResponseDTOMapper creditCardResponseDTOMapper;

    private final UserRepository userRepository;

    private final Clock clock;

    public CreditCardResponseDTO getCard(Long cardId, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> UserNotFoundException.ofUserId(ErrorCode.USER_NOT_FOUND, String.valueOf(userId)));

        CreditCard creditCard = paymentRepository.findById(cardId)
                .orElseThrow(() -> new CardNotFoundException(ErrorConstants.CARD_NOT_FOUND_MSG));

        CredentialChecker.ifUserHasTheCardProceedOrElseException(user, creditCard);

        return creditCardResponseDTOMapper.apply(creditCard);
    }

    public void register(CreditCardRequestDTO cardRequest, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> UserNotFoundException.ofUserId(ErrorCode.USER_NOT_FOUND, String.valueOf(userId)));

        CreditCard creditCard = CreditCardFactory.makeCard(cardRequest);

        CreditCardValidator.luhnValidate(creditCard, clock);

        CreditCardValidator.validateCardByVendor(creditCard);

        if (Boolean.TRUE.equals(creditCard.getIsActive()) && user.getCards().stream().anyMatch(CreditCard::getIsActive)) {
            CreditCard previouslyActiveCard = paymentRepository.findActiveCardUnderUserById(userId)
                    .orElseThrow(() ->
                            StateMisMatchException.ofCode(ErrorCode.STATE_MISMATCH));

            previouslyActiveCard.setIsActive(false);
            paymentRepository.save(previouslyActiveCard);
        }
        creditCard.setUserId(userId);

        paymentRepository.save(creditCard);
    }

    public void updateCard(CreditCardRequestDTO cardRequest, Long cardId, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> UserNotFoundException.ofUserId(ErrorCode.USER_NOT_FOUND, String.valueOf(userId)));

        CreditCard creditCard = paymentRepository.findById(cardId)
                        .orElseThrow(() -> new CardNotFoundException(ErrorConstants.CARD_NOT_FOUND_MSG));

        CredentialChecker.ifUserHasTheCardProceedOrElseException(user, creditCard);

        String cardNumber = cardRequest.cardNumber();
        YearMonth expiryDate = cardRequest.expiryDate();
        Boolean isActive = cardRequest.isActive();
        String fullName = cardRequest.fullName();

        if (cardNumber != null && ! creditCard.getCardNumber().equals(cardNumber)) {
            creditCard.setCardNumber(cardNumber);
        }
        if (expiryDate != null && expiryDate.isAfter(YearMonth.from(ZonedDateTime.now(clock)))) {
            creditCard.setExpiryDate(expiryDate);
        }
        if (isActive != null && ! creditCard.getIsActive().equals(isActive)) {
            if (isActive) {
                CreditCard previouslyActiveCard = paymentRepository.findActiveCardUnderUserById(userId)
                        .orElseThrow(() ->
                                StateMisMatchException.ofCode(ErrorCode.STATE_MISMATCH));

                previouslyActiveCard.setIsActive(false);
                paymentRepository.save(previouslyActiveCard);
            }
            creditCard.setIsActive(isActive);
        }
        if (fullName != null && ! creditCard.getFullName().equals(fullName)) {
            creditCard.setFullName(fullName);
        }

        CreditCardValidator.luhnValidate(creditCard, clock);

        CreditCardValidator.validateCardByVendor(creditCard);

        paymentRepository.save(creditCard);
    }

    public void deleteCard(Long cardId, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> UserNotFoundException.ofUserId(ErrorCode.USER_NOT_FOUND, String.valueOf(userId)));

        CreditCard creditCard = paymentRepository.findById(cardId)
                .orElseThrow(() -> new CardNotFoundException(ErrorConstants.CARD_NOT_FOUND_MSG));

        CredentialChecker.ifUserHasTheCardProceedOrElseException(user, creditCard);

        user.deleteCard(creditCard);

        userRepository.save(user);
        paymentRepository.delete(creditCard);
    }
}
