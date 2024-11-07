package hhplus.booking.app.queue.infra;

import hhplus.booking.app.queue.domain.repository.QueueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Set;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
@Primary
public class RedisQueueRepository implements QueueRepository {

    private static final String WAITING_QUEUE = "queue:waiting";
    private static final String PROCESSING_TOKEN = "processingToken";
    private static final Long EXPIRED_SECONDS = 60L;
    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public String registerQueue() {

        String queueTokenValue = UUID.randomUUID().toString();

        LocalDateTime nowKST = LocalDateTime.now(ZoneOffset.ofHours(9));  // 한국 시간(KST) 기준
        long timestamp = nowKST.toEpochSecond(ZoneOffset.ofHours(9));  // 시간(초 단위)

        redisTemplate.opsForZSet().add(WAITING_QUEUE, queueTokenValue, timestamp);

        return queueTokenValue;
    }

    @Override
    public Long getQueueRank(String queueTokenValue) {

        if (queueTokenValue == null || queueTokenValue.trim().isEmpty()) {
            queueTokenValue = registerQueue();
        }

        String processTokenValue = redisTemplate.opsForValue().get(PROCESSING_TOKEN + ":" + queueTokenValue);

        if (processTokenValue != null) {
            return 0L;
        }

        return redisTemplate.opsForZSet().rank("queue:waiting", queueTokenValue);
    }

    @Override
    public long getProcessingQueueCount() {
        Set<String> keys = redisTemplate.keys("processingToken:*");
        return keys != null ? keys.size() : 0;
    }

    @Override
    public long activateWaitingToken(long updateCount) {

        Set<String> waitingTokens = redisTemplate.opsForZSet().range(WAITING_QUEUE, 0, updateCount);
        long successUpdatedCount = 0;

        if (waitingTokens != null) {
            for (String token : waitingTokens) {
                redisTemplate.opsForValue().set(PROCESSING_TOKEN + ":" + token, token, Duration.ofMinutes(EXPIRED_SECONDS));
                redisTemplate.opsForZSet().remove(WAITING_QUEUE, token);
                successUpdatedCount ++;
            }
        }
        return successUpdatedCount;
    }

    @Override
    public void deleteProcessingToken(String processingToken) {
        redisTemplate.opsForValue().getAndDelete(processingToken);
    }
}
