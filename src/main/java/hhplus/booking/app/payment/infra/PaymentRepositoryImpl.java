package hhplus.booking.app.payment.infra;

import hhplus.booking.app.payment.domain.entity.Payment;
import hhplus.booking.app.payment.domain.repository.PaymentRepository;
import hhplus.booking.app.payment.infra.jpa.PaymentJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PaymentRepositoryImpl implements PaymentRepository {

    private final PaymentJpaRepository paymentJpaRepository;


    @Override
    public Payment savePayment(Long concertBookingId, Long price) {
        return paymentJpaRepository.save(Payment.of(concertBookingId, price));
    }
}
