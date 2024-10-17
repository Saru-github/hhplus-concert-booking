package hhplus.booking.app.payment.domain.repository;

import hhplus.booking.app.payment.domain.entity.Payment;

public interface PaymentRepository {
    Payment savePayment(Long concertBookingId, Long price);
}
