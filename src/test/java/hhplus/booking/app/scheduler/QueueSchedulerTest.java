package hhplus.booking.app.scheduler;

import hhplus.booking.app.queue.domain.entity.Queue;
import hhplus.booking.app.queue.domain.repository.QueueRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.BDDMockito.*;
import static org.assertj.core.api.Assertions.*;

class QueueSchedulerTest {

    @Mock
    private QueueRepository queueRepository;

    @InjectMocks
    private QueueScheduler queueScheduler;

    private final long MAX_QUEUE_VALUE = 10L;

    @Test
    @DisplayName("[성공] 대기열 진행 중 상태의 크기가 10 일 때, 스케쥴러를 통해 WAITING 1명 추가 테스트")
    void test() {

        Queue queue1 = new Queue(1L, "TEST_TOKEN", "COMPLETED", LocalDateTime.now().plusMinutes(5), LocalDateTime.now(), LocalDateTime.now());
        Queue queue2 = new Queue(2L, "TEST_TOKEN", "COMPLETED", LocalDateTime.now().plusMinutes(5), LocalDateTime.now(), LocalDateTime.now());
        Queue queue3 = new Queue(3L, "TEST_TOKEN", "PROCESSING", LocalDateTime.now().plusMinutes(5), LocalDateTime.now(), LocalDateTime.now());
        Queue queue4 = new Queue(4L, "TEST_TOKEN", "PROCESSING", LocalDateTime.now().plusMinutes(5), LocalDateTime.now(), LocalDateTime.now());
        Queue queue5 = new Queue(5L, "TEST_TOKEN", "PROCESSING", LocalDateTime.now().plusMinutes(5), LocalDateTime.now(), LocalDateTime.now());
        Queue queue6 = new Queue(6L, "TEST_TOKEN", "PROCESSING", LocalDateTime.now().plusMinutes(5), LocalDateTime.now(), LocalDateTime.now());
        Queue queue7 = new Queue(7L, "TEST_TOKEN", "PROCESSING", LocalDateTime.now().plusMinutes(5), LocalDateTime.now(), LocalDateTime.now());
        Queue queue8 = new Queue(8L, "TEST_TOKEN", "PROCESSING", LocalDateTime.now().plusMinutes(5), LocalDateTime.now(), LocalDateTime.now());
        Queue queue9 = new Queue(9L, "TEST_TOKEN", "PROCESSING", LocalDateTime.now().plusMinutes(5), LocalDateTime.now(), LocalDateTime.now());
        Queue queue10 = new Queue(10L, "TEST_TOKEN", "WAITING", LocalDateTime.now().plusMinutes(5), LocalDateTime.now(), LocalDateTime.now());
        Queue queue11 = new Queue(10L, "TEST_TOKEN", "WAITING", LocalDateTime.now().plusMinutes(5), LocalDateTime.now(), LocalDateTime.now());

        // given

        given(queueRepository.findWaitingQueues("WAITING")).willReturn(List.of(queue1, queue2, queue3, queue4, queue5, queue6, queue7, queue8, queue9));
        long waitingQueueCount = queueRepository.findWaitingQueues("WAITING").size();
        long updateQueueQty = MAX_QUEUE_VALUE - waitingQueueCount;


        // when
        List<Long> result = queueScheduler.enterProcessingScheduler().stream()
                .map(Queue::getQueueId)
                .toList();


        // then
        assertThat(result).isEqualTo(List.of(10L));

    }
}