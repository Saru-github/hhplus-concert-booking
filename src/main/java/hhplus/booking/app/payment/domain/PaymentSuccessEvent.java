package hhplus.booking.app.payment.domain;

public record PaymentSuccessEvent(
        String topic,
        Long concertBookingId,
        Long price
) {}
