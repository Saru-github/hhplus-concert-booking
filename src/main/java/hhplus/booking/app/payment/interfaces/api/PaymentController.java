package hhplus.booking.app.payment.interfaces.api;

import hhplus.booking.app.payment.interfaces.api.dto.PaymentInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/points")
public class PaymentController {

    @GetMapping("/{userId}")
    public ResponseEntity<PaymentInfo.Output> getUserPoint(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestBody PaymentInfo.Input input) {

        return ResponseEntity.ok(PaymentInfo.Output.builder()
                .pointId(1L)
                .userId(1L)
                .balance(10000L)
                .build());
    }

    @PutMapping("/{userId}/charge")
    public ResponseEntity<PaymentInfo.Output> chargePoints(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestBody PaymentInfo.Input input) {

        return ResponseEntity.ok(PaymentInfo.Output.builder()
                        .pointId(1L)
                        .userId(1L)
                        .balance(10000L)
                        .build());
    }
}
