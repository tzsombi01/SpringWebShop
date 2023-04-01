package com.tzsombi.webshop.controllers;

import com.tzsombi.webshop.models.CreditCardRequestDTO;
import com.tzsombi.webshop.models.CreditCardResponseDTO;
import com.tzsombi.webshop.services.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/payments")
public class PaymentController {

    private final PaymentService paymentService;

    @GetMapping("/retrieve/{cardId}")
    public ResponseEntity<CreditCardResponseDTO> getCard(@PathVariable Long cardId, @RequestParam Long userId) {
        return new ResponseEntity<>(paymentService.getCard(cardId, userId), HttpStatus.OK);
    }

    @PostMapping("/register/{userId}")
    public ResponseEntity<String> registerCard(@PathVariable Long userId,
                                               @RequestBody CreditCardRequestDTO cardRequest) {
        paymentService.register(cardRequest, userId);
        return new ResponseEntity<>("Card registered successfully!", HttpStatus.ACCEPTED);
    }

    @PutMapping("update/{cardId}")
    public ResponseEntity<String> updateCard(@PathVariable Long cardId,
                                             @RequestParam Long userId,
                                             @RequestBody CreditCardRequestDTO cardRequestDTO) {
        paymentService.updateCard(cardRequestDTO, cardId, userId);
        return new ResponseEntity<>("Card updated successfully!", HttpStatus.OK);
    }

    @DeleteMapping("/delete/{cardId}")
    public ResponseEntity<String> deleteCard(@PathVariable Long cardId, @RequestParam Long userId) {
        paymentService.deleteCard(cardId, userId);
        return new ResponseEntity<>("Card was deleted successfully!", HttpStatus.OK);
    }
}
