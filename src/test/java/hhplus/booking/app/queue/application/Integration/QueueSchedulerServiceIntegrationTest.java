package hhplus.booking.app.queue.application.Integration;

import hhplus.booking.app.queue.application.QueueSchedulerService;
import hhplus.booking.app.queue.application.QueueService;
import hhplus.booking.app.queue.application.dto.QueueInfo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class QueueSchedulerServiceIntegrationTest {

    @Autowired
    private QueueSchedulerService queueSchedulerService;

    @Autowired
    private QueueService queueService;

    @Test
    @DisplayName("[성공] 대기열 삭제 스케쥴러 이후 52번 대기열 삭제되어, 53번 대기열 순위 1 감소")
    void testConcurrentQueueInfo() throws Exception {

        // given
        QueueInfo.Output result1 = queueService.getQueueInfo(new QueueInfo.Input(null));
        QueueInfo.Output result2 = queueService.getQueueInfo(new QueueInfo.Input(null));


        // when
        queueSchedulerService.execute();

        QueueInfo.Output result1afterBatch = queueService.getQueueInfo(new QueueInfo.Input(result1.tokenValue()));
        QueueInfo.Output result2afterBatch = queueService.getQueueInfo(new QueueInfo.Input(result2.tokenValue()));

        // when

        QueueInfo.Output result53AfterExecuteScheduler = queueService.getQueueInfo(new QueueInfo.Input("Bearer TEST_TOKEN2"));

        // then
        assertThat(result53BeforeExecuteScheduler.rank()).isEqualTo(43L);
        assertThat(result53AfterExecuteScheduler.rank()).isEqualTo(42L);

    }
}
