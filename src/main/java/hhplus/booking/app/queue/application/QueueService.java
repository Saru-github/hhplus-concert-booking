package hhplus.booking.app.queue.application;

import hhplus.booking.app.queue.application.dto.QueueInfo;
import hhplus.booking.app.queue.domain.entity.Queue;
import hhplus.booking.app.queue.domain.repository.QueueRepository;
import hhplus.booking.app.queue.infra.jpa.QueueJpaRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class QueueService {

    @Qualifier("redisQueueRepository")
    private final QueueRepository queueRepository;
    private final QueueJpaRepository queueJpaRepository;

    @Transactional
    public QueueInfo.Output getQueueInfo(QueueInfo.Input input) {

        String tokenValue = input.authorizationHeader();

        if (tokenValue == null || tokenValue.isBlank()) {
            tokenValue = queueRepository.registerQueue();
        } else {
            // "Bearer " 제거
            tokenValue = tokenValue.substring(7);
        }

        Long rank = queueRepository.getQueueRank(tokenValue);
        String status = rank > 0 ? "WAITING" : "PROCESSING";

        return QueueInfo.Output.builder()
                .tokenValue(tokenValue)
                .rank(rank)
                .status(status)
                .build();
    }
}
