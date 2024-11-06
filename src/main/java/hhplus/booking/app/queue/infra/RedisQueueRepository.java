package hhplus.booking.app.queue.infra;

import hhplus.booking.app.queue.domain.entity.Queue;
import hhplus.booking.app.queue.domain.repository.QueueRepository;
import hhplus.booking.config.exception.BusinessException;
import hhplus.booking.config.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Repository;

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

    private static final String QUEUE_KEY = "queue";  // Redis에 저장할 키 이름
    private final RedisTemplate<String, String> redisTemplate;

    // RedisTemplate에서 ZSetOperations 가져오기
    private final ZSetOperations<String, String> zSetOperations;

    @Override
    public Queue registerQueue() {
        // UUID로 QueueToken을 생성
        String queueTokenValue = UUID.randomUUID().toString();

        LocalDateTime nowKST = LocalDateTime.now(ZoneOffset.ofHours(9));  // 한국 시간(KST) 기준
        long timestamp = nowKST.toEpochSecond(ZoneOffset.ofHours(9));  // 시간(초 단위)

        // Redis에 대기 중인 큐를 추가. 점수는 현재 시간으로 설정.
        redisTemplate.opsForZSet().add("queue:waiting", queueTokenValue, timestamp);
        redisTemplate.opsForHash().put("queue:status", queueTokenValue, "WAITING");

        // Queue 객체 반환
        return Queue.from(queueTokenValue);  // 정상적으로 생성된 Queue를 반환
    }

    @Override
    public Queue getQueue(String queueTokenValue) {
        // queueTokenValue가 공백일 경우 새 큐를 생성해서 반환
        if (queueTokenValue == null || queueTokenValue.trim().isEmpty()) {
            return registerQueue();  // 빈 토큰이 들어오면 새 토큰을 생성해서 반환
        }

        // Redis에서 큐를 가져옵니다. 존재하지 않으면 예외를 던짐.
        boolean exists = redisTemplate.opsForZSet().rank("queue:waiting", queueTokenValue) != null;
        if (!exists) {
            throw new BusinessException(ErrorCode.INVALID_TOKEN);  // 유효하지 않은 토큰이면 예외 처리
        }

        return Queue.from(queueTokenValue);  // 존재하는 큐는 그대로 반환
    }

    @Override
    public long findWaitingQueues(String queueTokenValue) {
        Long rank = redisTemplate.opsForZSet().rank("queue:waiting", queueTokenValue);
        if (rank != null) {
            return rank + 1;  // rank는 0부터 시작하므로 +1하여 반환
        } else {
            throw new BusinessException(ErrorCode.INVALID_TOKEN);  // rank가 null인 경우 예외
        }
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
        // 만료된 큐들을 삭제합니다.
        LocalDateTime now = LocalDateTime.now();
        Set<String> expiredQueues = redisTemplate.opsForZSet().rangeByScore(QUEUE_KEY, 0, now.toEpochSecond(java.time.ZoneOffset.UTC));

        for (String queueTokenValue : expiredQueues) {
            redisTemplate.opsForZSet().remove(QUEUE_KEY, queueTokenValue);
        }
    }
}
