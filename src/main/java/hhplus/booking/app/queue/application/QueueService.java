package hhplus.booking.app.queue.application;

import hhplus.booking.app.queue.domain.entity.Queue;
import hhplus.booking.app.queue.domain.repository.QueueRepository;
import hhplus.booking.app.queue.application.dto.QueueInfo;
import hhplus.booking.config.security.BcryptEncryptor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class QueueService {

    private final QueueRepository queueRepository;

    public QueueInfo.Output getUserQueueRank(QueueInfo.Input input) {

        String queueTokenValue = input.authorizationHeader();

        if (queueTokenValue.isBlank()) {
            queueTokenValue = UUID.randomUUID().toString();
            queueRepository.registerQueue(queueTokenValue);
        }

        Queue queue = queueRepository.getQueue(queueTokenValue);

        return new QueueInfo.Output(queue.tokenValue, queue.rank, queue.status);

    }
}
