package hhplus.booking.app.payment.infra;

import hhplus.booking.app.payment.domain.entity.Payment;
import hhplus.booking.app.payment.domain.repository.PaymentRepository;
import hhplus.booking.app.payment.infra.jpa.PaymentJpaRepository;
import hhplus.booking.app.queue.domain.entity.Queue;
import hhplus.booking.app.queue.domain.repository.QueueRepository;
import hhplus.booking.app.queue.infra.jpa.QueueJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class PaymentRepositoryImpl implements PaymentRepository {

    private final PaymentJpaRepository paymentJpaRepository;


    @Override
    public Payment savePayment(Long userId, Long concertSeatId, Long price) {
        return paymentJpaRepository.save(Payment.of(userId, concertSeatId, price));
    }
}
