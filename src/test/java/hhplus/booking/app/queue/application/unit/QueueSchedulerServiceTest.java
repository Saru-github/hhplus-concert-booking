package hhplus.booking.app.queue.application.unit;

import hhplus.booking.app.queue.application.QueueSchedulerService;
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
class QueueSchedulerServiceTest {

    @Mock
    private QueueRepository queueRepository;

    @InjectMocks
    private QueueSchedulerService queueSchedulerService;

    @Test
    @DisplayName("[성공] 대기열 진행 중 상태의 크기가 10 일 때, 스케쥴러를 통해 WAITING 2명 추가 테스트")
    void successTestQueueScheduler() {

        Queue queue1 = new Queue(1L, "TEST_TOKEN", "WAITING", LocalDateTime.now().plusMinutes(5), LocalDateTime.now(), LocalDateTime.now());
        Queue queue2 = new Queue(2L, "TEST_TOKEN", "WAITING", LocalDateTime.now().plusMinutes(5), LocalDateTime.now(), LocalDateTime.now());

        // given
        given(queueRepository.getProcessingQueueCount()).willReturn(2L);
        given(queueRepository.findWaitingQueues()).willReturn(List.of(queue1, queue2));

        // when
        List<Queue> result = queueSchedulerService.enterProcessingScheduler();

        // then
        assertThat(result.get(0).getQueueId()).isEqualTo(1L);
        assertThat(result.get(0).getStatus()).isEqualTo("PROCESSING");
    }

    @Test
    @DisplayName("[성공] 대기열에 등록된 토큰의 만료시간이 지났을 때, 대기열 토큰이 삭제되는지 확인")
    void successTestDeleteQueueScheduler() {

        LocalDateTime expiredQueue1Time = LocalDateTime.now().plusMinutes(5);

        Queue queue1 = new Queue(1L, "TEST_TOKEN", "WAITING", expiredQueue1Time, LocalDateTime.now(), LocalDateTime.now());
        Queue queue2 = new Queue(2L, "TEST_TOKEN", "WAITING", LocalDateTime.now(), LocalDateTime.now(), LocalDateTime.now());

        // given
        given(queueRepository.findDeleteExpiredQueues()).willReturn(List.of(queue2));

        // when
        List<Queue> result = queueSchedulerService.deleteQueueScheduler();

        // then
        assertThat(result.get(0).getQueueId()).isEqualTo(2L);
        assertThat(queue1.getExpiredAt()).isEqualTo(expiredQueue1Time);
    }
}