package hhplus.booking.app.queue.application;

import hhplus.booking.app.queue.application.dto.QueueInfo;
import hhplus.booking.app.queue.domain.entity.Queue;
import hhplus.booking.app.queue.domain.repository.QueueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class QueueService {

    private final QueueRepository queueRepository;

    public QueueInfo.Output getUserQueueRank(QueueInfo.Input input) {

        String tokenValue = input.authorizationHeader();

        if (tokenValue == null || tokenValue.isBlank()) {
            tokenValue = queueRepository.registerQueue();
        } else {
            // "Bearer " 제거
            tokenValue = tokenValue.substring(7);
        }

        Queue queue = queueRepository.getQueue(tokenValue);
        long rank = 0;

        if ("WAITING".equals(queue.getStatus())) {
            queue.refreshExpiration();
            rank = queueRepository.findWaitingQueues(queue).stream()
                    .sorted(Comparator.comparing(Queue::getCreatedAt))
                    .toList()
                    .indexOf(queue) + 1;
        }

        return QueueInfo.Output.builder().
                queueId(queue.getQueueId())
                .tokenValue(queue.getTokenValue())
                .rank(rank)
                .status(queue.getStatus())
                .build();
    }
}
