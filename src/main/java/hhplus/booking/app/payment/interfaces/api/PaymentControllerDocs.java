package hhplus.booking.app.payment.interfaces.api;

import hhplus.booking.app.payment.application.dto.PaymentInfo;
import hhplus.booking.app.queue.application.dto.QueueInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Payment", description = "결제 관련 API")
public interface PaymentControllerDocs {

    @Operation(summary = "결제 진행", description = "결제를 진행하고 결제 완료 후 결제 정보를 반환합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "결제 완료 및 결제 정보 반환"),
    })
    public ResponseEntity<PaymentInfo.Output> processPayment(
            @RequestHeader(value = "Authorization", required = true) String authorizationHeader,
            @RequestParam("concertBookingId") Long concertBookingId
    );
}