package hhplus.booking.app.scheduler.unit;

import hhplus.booking.app.concert.domain.entity.ConcertBooking;
import hhplus.booking.app.concert.domain.entity.ConcertSeat;
import hhplus.booking.app.concert.domain.repository.ConcertRepository;
import hhplus.booking.app.queue.domain.entity.Queue;
import hhplus.booking.app.queue.domain.repository.QueueRepository;
import hhplus.booking.app.scheduler.QueueScheduler;
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
class QueueSchedulerConfigTest {

    @Mock
    private QueueRepository queueRepository;

    @Mock
    private ConcertRepository concertRepository;

    @InjectMocks
    private QueueScheduler queueScheduler;

    @Test
    @DisplayName("[성공] 대기열 진행 중 상태의 크기가 10 일 때, 스케쥴러를 통해 WAITING 2명 추가 테스트")
    void successTestQueueScheduler() {

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

    @Test
    @DisplayName("[성공] 임시예약된 콘서트 티켓의 만료시간이 지났을 때, 티켓이 만료되고 좌석이 예약가능 상태로 변경 되는지 테스트")
    void successTestBookingScheduler() {

        ConcertSeat concertSeat = new ConcertSeat(1L, 1L, 1L, 10000L, "BOOKED");
        ConcertBooking concertBooking = new ConcertBooking(1L, 1L, 1L,"BOOKED", LocalDateTime.now());

        // given
        given(concertRepository.getExpiredBookings()).willReturn(List.of(concertBooking));
        given(concertRepository.getConcertSeat(1L)).willReturn(concertSeat);

        // when
        List<ConcertBooking> result = queueScheduler.expiredConcertBookingScheduler();

        // then
        assertThat(result.get(0).getStatus()).isEqualTo("EXPIRED");
        assertThat(concertSeat.getStatus()).isEqualTo("AVAILABLE");
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
        List<Queue> result = queueScheduler.deleteQueueScheduler();

        // then
        assertThat(result.get(0).getQueueId()).isEqualTo(2L);
        assertThat(queue1.getExpiredAt()).isEqualTo(expiredQueue1Time);
    }
}