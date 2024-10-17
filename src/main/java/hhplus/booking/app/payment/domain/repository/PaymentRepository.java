package hhplus.booking.app.payment.domain.repository;

import hhplus.booking.app.payment.domain.entity.Payment;
import hhplus.booking.app.queue.domain.entity.Queue;

import java.util.List;

public interface PaymentRepository {
    Payment savePayment(Long userId, Long concertSeatId, Long price);
}
