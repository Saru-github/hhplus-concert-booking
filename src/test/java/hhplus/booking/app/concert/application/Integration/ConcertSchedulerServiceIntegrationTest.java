package hhplus.booking.app.concert.application.Integration;

import hhplus.booking.app.concert.application.ConcertSchedulerService;
import hhplus.booking.app.concert.application.ConcertService;
import hhplus.booking.app.concert.application.dto.ConcertSeatInfo;
import hhplus.booking.app.concert.domain.entity.ConcertBooking;
import hhplus.booking.app.concert.infra.jpa.ConcertBookingJpaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ConcertSchedulerServiceIntegrationTest {

    @Autowired
    private ConcertSchedulerService concertSchedulerService;

    @Autowired
    private ConcertBookingJpaRepository concertBookingJpaRepository;

    @Autowired
    private ConcertService concertService;

    @Test
    @DisplayName("[성공] 임시예약 상태인 1번좌석에 대해 스케쥴러를 통해 임시예약 만료뒤, 예약 가능 좌석에 노출되는 지 확인")
    void testConcurrentQueueInfo() throws Exception {

        // given
        ConcertBooking concertBooking = new ConcertBooking(null, 1L, 1L,"BOOKED", LocalDateTime.now());
        concertBookingJpaRepository.save(concertBooking);

        ConcertSeatInfo.Output result1seatBeforeExecuteScheduler = concertService.getAvailableConcertSeatInfos(new ConcertSeatInfo.Input(1L)).get(0);

        // when
        concertSchedulerService.execute();
        ConcertSeatInfo.Output result1seatAfterExecuteScheduler = concertService.getAvailableConcertSeatInfos(new ConcertSeatInfo.Input(1L)).get(0);

        // then
        assertThat(result1seatBeforeExecuteScheduler.concertSeatId()).isEqualTo(11L);
        assertThat(result1seatAfterExecuteScheduler.concertSeatId()).isEqualTo(1L);
    }
}
