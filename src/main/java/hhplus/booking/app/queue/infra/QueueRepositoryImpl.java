package hhplus.booking.app.queue.infra;

import hhplus.booking.app.queue.domain.entity.Queue;
import hhplus.booking.app.queue.domain.repository.QueueRepository;
import hhplus.booking.app.queue.infra.jpa.QueueJpaRepository;
import hhplus.booking.config.exception.BusinessException;
import hhplus.booking.config.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class QueueRepositoryImpl implements QueueRepository {

    private final QueueJpaRepository queueJpaRepository;

    @Override
    public Queue registerQueue() {
        String queueTokenValue = UUID.randomUUID().toString();
        return queueJpaRepository.save(Queue.from(queueTokenValue));
    }

    @Override
    public Queue getQueue(String tokenValue) {
        return queueJpaRepository.findByTokenValue(tokenValue).orElseThrow(() -> new BusinessException(ErrorCode.INVALID_TOKEN));
    }


    @Override
    public List<Queue> findWaitingQueues() {
        return queueJpaRepository.findWaitingQueues("WAITING");
    }

    @Override
    public long getProcessingQueueCount() {
        return queueJpaRepository.countByStatus("PROCESSING");
    }

    @Override
    public List<Queue> findDeleteExpiredQueues() {
        return queueJpaRepository.findDeleteByExpiredAtBefore(LocalDateTime.now());
    }

    @Override
    public void deleteExpiredQueues() {
        queueJpaRepository.deleteByExpiredAtBefore(LocalDateTime.now());
    }
}
