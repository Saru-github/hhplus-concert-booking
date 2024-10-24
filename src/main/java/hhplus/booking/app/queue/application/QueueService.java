package hhplus.booking.app.queue.application;

import hhplus.booking.app.queue.application.dto.QueueInfo;
import hhplus.booking.app.queue.domain.entity.Queue;
import hhplus.booking.app.queue.domain.repository.QueueRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class QueueService {

    private final QueueRepository queueRepository;

    @Transactional
    public QueueInfo.Output getQueueInfo(QueueInfo.Input input) {

        String tokenValue = input.authorizationHeader();

        if (tokenValue == null || tokenValue.isBlank()) {
            tokenValue = queueRepository.registerQueue().getTokenValue();
        } else {
            // "Bearer " 제거
            tokenValue = tokenValue.substring(7);
        }

        Queue queue = queueRepository.getQueue(tokenValue);
        long rank = 0;

        if ("WAITING".equals(queue.getStatus())) {
            List<Queue> waitingQueues = queueRepository.findWaitingQueues();
            rank = waitingQueues.indexOf(queue) + 1;
            queue.refreshExpiration();
        }

        return QueueInfo.Output.builder()
                .queueId(queue.getQueueId())
                .tokenValue(queue.getTokenValue())
                .rank(rank)
                .status(queue.getStatus())
                .build();
    }
}
