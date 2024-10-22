package hhplus.booking.app.scheduler.Integration;

import hhplus.booking.app.queue.domain.entity.Queue;
import hhplus.booking.app.queue.domain.repository.QueueRepository;
import hhplus.booking.app.queue.infra.jpa.QueueJpaRepository;
import hhplus.booking.app.scheduler.QueueScheduler;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class QueueSchedulerConfigIntegrationTest {

    @Autowired
    private QueueScheduler queueScheduler;

    @Autowired
    private QueueRepository queueRepository;

    @Autowired
    private QueueJpaRepository queueJpaRepository;

    @Test
    @DisplayName("[성공] 대기열 삭제 스케쥴러 이후, 대기열 삭제 성공")
    void testConcurrentQueueInfo() throws Exception {

        LocalDateTime expiredQueueTime = LocalDateTime.now().plusMinutes(5);

        Queue queue52 = new Queue("TEST_TOKEN", "WAITING", expiredQueueTime);
        Queue queue53 = new Queue("TEST_TOKEN", "WAITING", LocalDateTime.now());

        queueJpaRepository.save(queue52);
        queueJpaRepository.save(queue53);

        // when
        List<Queue> result = queueScheduler.deleteQueueScheduler();
        Optional<Queue> getQueue53result = queueJpaRepository.findById(53L);

        // then
        assertThat(result.get(0).getQueueId()).isEqualTo(53L);
        assertThat(getQueue53result).isEmpty();

    }
}
