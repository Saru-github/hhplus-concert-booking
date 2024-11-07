package hhplus.booking.app.queue.application;

import hhplus.booking.app.queue.application.dto.QueueInfo;
import hhplus.booking.app.queue.domain.repository.QueueRepository;
import hhplus.booking.config.exception.BusinessException;
import hhplus.booking.config.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class QueueService {

    @Qualifier("redisQueueRepository")
    private final QueueRepository queueRepository;

    @Transactional
    public QueueInfo.Output getQueueInfo(QueueInfo.Input input) {

        String tokenValue = input.authorizationHeader();

        if (tokenValue == null || tokenValue.isBlank()) {
            tokenValue = queueRepository.registerQueue();
        } else {
            // "Bearer " 제거
            if (tokenValue.startsWith("Bearer ")) {
                tokenValue = tokenValue.substring(7);
            }
        }

        Long rank = queueRepository.getQueueRank(tokenValue);

        if (rank == null) {
            throw new BusinessException(ErrorCode.INVALID_TOKEN);
        }

        String status = rank > 0 ? "WAITING" : "PROCESSING";

        return QueueInfo.Output.builder()
                .tokenValue(tokenValue)
                .rank(rank)
                .status(status)
                .build();
    }
}
