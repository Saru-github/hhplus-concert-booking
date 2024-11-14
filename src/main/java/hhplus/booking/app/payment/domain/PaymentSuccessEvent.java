package hhplus.booking.app.payment.domain;

public record PaymentSuccessEvent(
        Long paymentId, Long concertBookingId
) {}
