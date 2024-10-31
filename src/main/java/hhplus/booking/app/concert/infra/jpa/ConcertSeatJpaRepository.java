package hhplus.booking.app.concert.infra.jpa;

import hhplus.booking.app.concert.domain.entity.ConcertSeat;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConcertSeatJpaRepository extends JpaRepository<ConcertSeat, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<ConcertSeat> findById(Long id);
    List<ConcertSeat> findAllByConcertScheduleIdAndStatus(Long concertScheduleId, String status);

}
