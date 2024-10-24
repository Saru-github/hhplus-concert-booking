package hhplus.booking.app.queue.application.Integration;

import hhplus.booking.app.queue.application.QueueSchedulerService;
import hhplus.booking.app.queue.application.QueueService;
import hhplus.booking.app.queue.application.dto.QueueInfo;
import hhplus.booking.app.queue.domain.entity.Queue;
import hhplus.booking.app.queue.infra.jpa.QueueJpaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class QueueSchedulerServiceIntegrationTest {

    @Autowired
    private QueueSchedulerService queueSchedulerService;

    @Autowired
    private QueueJpaRepository queueJpaRepository;

    @Autowired
    private QueueService queueService;

    @Test
    @DisplayName("[성공] 대기열 삭제 스케쥴러 이후 52번 대기열 삭제되어, 53번 대기열 순위 1 감소")
    void testConcurrentQueueInfo() throws Exception {

        Queue queue52 = new Queue("TEST_TOKEN", "WAITING", LocalDateTime.now());
        Queue queue53 = new Queue("TEST_TOKEN2", "WAITING", LocalDateTime.now().plusMinutes(5));

        queueJpaRepository.save(queue52);
        queueJpaRepository.save(queue53);

        QueueInfo.Output result53BeforeExecuteScheduler = queueService.getQueueInfo(new QueueInfo.Input("Bearer TEST_TOKEN2"));

        // when
        queueSchedulerService.execute();

        QueueInfo.Output result53AfterExecuteScheduler = queueService.getQueueInfo(new QueueInfo.Input("Bearer TEST_TOKEN2"));

        // then
        assertThat(result53BeforeExecuteScheduler.rank()).isEqualTo(43L);
        assertThat(result53AfterExecuteScheduler.rank()).isEqualTo(42L);

    }
}
