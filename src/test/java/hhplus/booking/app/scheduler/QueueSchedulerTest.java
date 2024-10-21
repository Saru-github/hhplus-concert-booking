package hhplus.booking.app.scheduler;

import hhplus.booking.app.queue.domain.entity.Queue;
import hhplus.booking.app.queue.domain.repository.QueueRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class QueueSchedulerTest {

    @Mock
    private QueueRepository queueRepository;

    @InjectMocks
    private QueueScheduler queueScheduler;

    private final long MAX_QUEUE_VALUE = 10L;

    @Test
    @DisplayName("[성공] 대기열 진행 중 상태의 크기가 10 일 때, 스케쥴러를 통해 WAITING 2명 추가 테스트")
    void test() {

        Queue queue1 = new Queue(1L, "TEST_TOKEN", "WAITING", LocalDateTime.now().plusMinutes(5), LocalDateTime.now(), LocalDateTime.now());
        Queue queue2 = new Queue(2L, "TEST_TOKEN", "WAITING", LocalDateTime.now().plusMinutes(5), LocalDateTime.now(), LocalDateTime.now());

        // given
        given(queueRepository.getProcessingQueueCount()).willReturn(2L);
        given(queueRepository.findWaitingQueues("WAITING")).willReturn(List.of(queue1, queue2));

        // when
        List<Queue> result = queueScheduler.enterProcessingScheduler();

        // then
        assertThat(result.get(0).getQueueId()).isEqualTo(1L);
        assertThat(result.get(0).getStatus()).isEqualTo("PROCESSING");
    }
}