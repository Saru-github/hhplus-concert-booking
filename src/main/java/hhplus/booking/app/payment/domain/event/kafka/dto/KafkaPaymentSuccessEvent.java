package hhplus.booking.app.payment.domain.event.kafka.dto;

import lombok.Builder;

@Builder
public record KafkaPaymentSuccessEvent(
        String userName,
        String concertName,
        String concertDate,
        Long seatNumber,
        Long price,
        Long concertBookingId,
        Long paymentId
) {}
