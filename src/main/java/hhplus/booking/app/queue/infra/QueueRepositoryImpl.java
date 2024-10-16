package hhplus.booking.app.queue.infra;

import hhplus.booking.app.queue.domain.entity.Queue;
import hhplus.booking.app.queue.domain.repository.QueueRepository;
import hhplus.booking.app.queue.infra.jpa.QueueJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class QueueRepositoryImpl implements QueueRepository {

    private final QueueJpaRepository queueJpaRepository;

    @Override
    public String registerQueue() {
        String queueTokenValue = UUID.randomUUID().toString();
        queueJpaRepository.save(Queue.from(queueTokenValue));
        return queueTokenValue;
    }

    @Override
    public Queue getQueue(String tokenValue) {
        return queueJpaRepository.findByTokenValue(tokenValue).orElse(Queue.from(registerQueue()));
    }


    @Override
    public List<Queue> findWaitingQueues(String status) {
        return queueJpaRepository.findWaitingQueues(status);
    }

    @Override
    public void deleteQueue(Long queueId) {
        queueJpaRepository.deleteByQueueId(queueId);
    }
}
