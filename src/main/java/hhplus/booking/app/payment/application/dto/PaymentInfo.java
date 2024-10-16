package hhplus.booking.app.payment.application.dto;

import lombok.Builder;

public class PaymentInfo {

    public record Input(Long concertBookingId, String authorizationHeader) {}
    @Builder
    public record Output(Long paymentId) {}
}
