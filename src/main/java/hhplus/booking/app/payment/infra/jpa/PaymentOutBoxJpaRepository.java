package hhplus.booking.app.payment.infra.jpa;

import hhplus.booking.app.payment.domain.entity.PaymentOutBox;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentOutBoxJpaRepository extends JpaRepository<PaymentOutBox, Long> {
}
