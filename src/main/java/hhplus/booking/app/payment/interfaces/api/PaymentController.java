package hhplus.booking.app.payment.interfaces.api;

import hhplus.booking.app.payment.application.PaymentUseCase;
import hhplus.booking.app.payment.application.dto.PaymentInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/booking/payments")
public class PaymentController implements PaymentControllerDocs{

    private final PaymentUseCase paymentUseCase;

    @PostMapping("/payment")
    public ResponseEntity<PaymentInfo.Output> processPayment(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestParam("concertBookingId") Long concertBookingId
    ) {
        return ResponseEntity.ok(paymentUseCase.processPayment(new PaymentInfo.Input(concertBookingId, authorizationHeader)));
    }
}
