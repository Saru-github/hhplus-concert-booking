package hhplus.booking.app.concert.infra.jpa;

import hhplus.booking.app.concert.domain.entity.ConcertBooking;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ConcertBookingJpaRepository extends JpaRepository<ConcertBooking, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<ConcertBooking> findById(Long id);
    List<ConcertBooking> findByStatusAndExpiredAtBefore(String status, LocalDateTime currentTime);
}
