package hhplus.booking.app.queue.infra.jpa;

import hhplus.booking.app.queue.domain.entity.Queue;
import hhplus.booking.app.queue.domain.repository.QueueRepository;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QueueJpaRepository extends JpaRepository<Queue, Long> {
    Optional<Queue> findByTokenValue(String tokenValue);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT q FROM Queue q WHERE q.status = :status ORDER BY q.createdAt ASC")
    List<Queue> findWaitingQueues(String status);
}
