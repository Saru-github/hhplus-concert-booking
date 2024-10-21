package hhplus.booking.app.queue.infra.jpa;

import hhplus.booking.app.queue.domain.entity.Queue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QueueJpaRepository extends JpaRepository<Queue, Long> {
    Optional<Queue> findByTokenValue(String tokenValue);

    @Query("SELECT q FROM Queue q WHERE q.status = :status ORDER BY q.createdAt ASC")
    List<Queue> findWaitingQueues(@Param("status") String status);

    void deleteByQueueId(Long tokenId);

    Long countByStatusProcessing();
}
