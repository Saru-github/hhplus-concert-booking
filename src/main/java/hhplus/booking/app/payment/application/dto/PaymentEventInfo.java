package hhplus.booking.app.payment.application.dto;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record PaymentEventInfo(
    String userName,
    String concertName,
    LocalDate concertDate,
    Long seatNumber,
    Long price,
    Long concertBookingId,
    Long paymentId
) {}
