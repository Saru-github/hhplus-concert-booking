package hhplus.booking.app.payment.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

public class PaymentInfo {

    public record Input(
            Long concertBookingId, String authorizationHeader
    ) {}

    @Builder
    @Schema(name = "PaymentOutput")
    public record Output(
            Long paymentId
    ) {}
}
