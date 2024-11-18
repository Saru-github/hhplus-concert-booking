package hhplus.booking.app.payment.domain;

public record PaymentSuccessEvent(
        String topic,
        Long paymentId,
        Long concertBookingId
) {}
