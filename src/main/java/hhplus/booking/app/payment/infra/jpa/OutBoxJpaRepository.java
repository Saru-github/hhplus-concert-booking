package hhplus.booking.app.payment.infra.jpa;

import hhplus.booking.app.payment.domain.entity.OutBox;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OutBoxJpaRepository extends JpaRepository<OutBox, Long> {
    List<OutBox> findAllByStatus(String Status);
}
