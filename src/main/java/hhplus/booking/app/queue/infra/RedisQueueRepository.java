package hhplus.booking.app.queue.infra;

import hhplus.booking.app.queue.domain.entity.Queue;
import hhplus.booking.app.queue.domain.repository.QueueRepository;
import hhplus.booking.config.exception.BusinessException;
import hhplus.booking.config.exception.ErrorCode;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
@Primary
public class RedisQueueRepository implements QueueRepository {

    private static final String WAITING_QUEUE = "queue:waiting";
    private static final String PROCESSING_QUEUE = "queue:processing";
    private static final Long EXPIRED_SECONDS = 60L;
    private final RedisTemplate<String, String> redisTemplate;

    @PostConstruct
    public void init() {
        if (redisTemplate != null) {
            // 서비스가 시작될 때, 큐에 TTL을 한 번만 설정
            redisTemplate.expire(WAITING_QUEUE, Duration.ofSeconds(EXPIRED_SECONDS));
        }
    }

    @Override
    public String registerQueue() {
        // UUID로 QueueToken을 생성
        String queueTokenValue = UUID.randomUUID().toString();

        LocalDateTime nowKST = LocalDateTime.now(ZoneOffset.ofHours(9));  // 한국 시간(KST) 기준
        long timestamp = nowKST.toEpochSecond(ZoneOffset.ofHours(9));  // 시간(초 단위)

        // Redis에 대기 중인 큐를 추가. 점수는 현재 시간으로 설정.
        redisTemplate.opsForZSet().add(WAITING_QUEUE, queueTokenValue, timestamp);
        redisTemplate.expire(WAITING_QUEUE, Duration.ofSeconds(EXPIRED_SECONDS));

        // Queue 객체 반환
        return queueTokenValue;
    }

    @Override
    public Long getQueueRank(String queueTokenValue) {
        // queueTokenValue가 공백일 경우 새 큐를 생성해서 반환
        if (queueTokenValue == null || queueTokenValue.trim().isEmpty()) {
            queueTokenValue = registerQueue();
        }

        Long queueRank = redisTemplate.opsForZSet().rank(PROCESSING_QUEUE, queueTokenValue);

        if (queueRank != null && queueRank >= 0L) {
            return 0L;
        }

        queueRank = redisTemplate.opsForZSet().rank("queue:waiting", queueTokenValue);

        if (queueRank == null) {
            throw new BusinessException(ErrorCode.INVALID_TOKEN);
        }

        return queueRank + 1;
    }

    @Override
    public long getProcessingQueueCount() {
        // 상태가 "PROCESSING"인 큐의 개수를 Redis Hash에서 상태를 조회하여 계산
        return redisTemplate.opsForHash().values("queue:status").stream()
                .filter(status -> "PROCESSING".equals(status.toString()))
                .count();
    }

    @Override
    public List<Queue> findDeleteExpiredQueues() {
        LocalDateTime now = LocalDateTime.now(ZoneOffset.ofHours(9));  // 한국 시간(KST) 기준
        long currentTimestamp = now.toEpochSecond(ZoneOffset.ofHours(9));  // 현재 시간을 초 단위로

        // Redis에서 만료된 큐를 조회 (예: 특정 시간 전에 생성된 큐)
        Set<String> expiredQueues = redisTemplate.opsForZSet().rangeByScore("queue:waiting", 0, currentTimestamp);

        return expiredQueues.stream()
                .map(Queue::from)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteExpiredQueues() {
//        // 만료된 큐들을 삭제합니다.
//        LocalDateTime now = LocalDateTime.now();
//        Set<String> expiredQueues = redisTemplate.opsForZSet().rangeByScore(QUEUE_KEY, 0, now.toEpochSecond(java.time.ZoneOffset.UTC));
//
//        for (String queueTokenValue : expiredQueues) {
//            redisTemplate.opsForZSet().remove(QUEUE_KEY, queueTokenValue);
//        }
    }
}
