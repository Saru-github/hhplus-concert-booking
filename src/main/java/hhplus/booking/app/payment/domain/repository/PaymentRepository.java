package hhplus.booking.app.payment.domain.repository;

import hhplus.booking.app.payment.domain.entity.Payment;
import hhplus.booking.app.payment.domain.entity.PaymentOutBox;

public interface PaymentRepository {
    Payment savePayment(Long concertBookingId, Long price);

    PaymentOutBox savePaymentOutBox(Long concertBookingId, Long price);
}
