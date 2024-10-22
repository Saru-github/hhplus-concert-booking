package hhplus.booking.app.concert.infra.jpa;

import hhplus.booking.app.concert.domain.entity.ConcertBooking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ConcertBookingJpaRepository extends JpaRepository<ConcertBooking, Long> {
    List<ConcertBooking> findByExpiredAtBefore(LocalDateTime currentTime);
}
