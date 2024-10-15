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

    private final QueueJpaRepository userJpaRepository;

    @Override
    public String registerQueue() {
        String queueTokenValue = UUID.randomUUID().toString();
        userJpaRepository.save(Queue.from(queueTokenValue));
        return queueTokenValue;
    }

    @Override
    public Queue getQueue(String tokenValue) {
        return userJpaRepository.findByTokenValue(tokenValue)
                .orElseGet(() -> userJpaRepository.findByTokenValue(registerQueue()).orElseThrow());
    }


    @Override
    public List<Queue> findWaitingQueues(Queue queue) {
        return userJpaRepository.findWaitingQueues(queue.getStatus());
    }
}
