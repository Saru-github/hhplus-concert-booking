package hhplus.booking.app.payment.domain.repository;

import hhplus.booking.app.payment.domain.entity.Payment;
import hhplus.booking.app.payment.domain.entity.OutBox;

public interface PaymentRepository {
    Payment savePayment(Long concertBookingId, Long price);

    OutBox savePaymentOutBox(Long concertBookingId, Long price);
}
