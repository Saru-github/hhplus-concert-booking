package hhplus.booking.app.concert.application.unit;

import hhplus.booking.app.concert.application.ConcertSchedulerService;
import hhplus.booking.app.concert.domain.entity.ConcertBooking;
import hhplus.booking.app.concert.domain.entity.ConcertSeat;
import hhplus.booking.app.concert.domain.repository.ConcertRepository;
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
class ConcertSchedulerServiceTest {

    @Mock
    private ConcertRepository concertRepository;

    @InjectMocks
    private ConcertSchedulerService concertSchedulerService;

    @Test
    @DisplayName("[성공] 임시예약된 콘서트 티켓의 만료시간이 지났을 때, 티켓이 만료되고 좌석이 예약가능 상태로 변경 되는지 테스트")
    void successTestBookingScheduler() {

        ConcertSeat concertSeat = new ConcertSeat(1L, 1L, 1L, 10000L, "BOOKED");
        ConcertBooking concertBooking = new ConcertBooking(1L, 1L, 1L,"BOOKED", LocalDateTime.now());

        // given
        given(concertRepository.getExpiredBookings()).willReturn(List.of(concertBooking));
        given(concertRepository.getConcertSeat(1L)).willReturn(concertSeat);

        // when
        List<ConcertBooking> result = concertSchedulerService.expiredConcertBookingScheduler();

        // then
        assertThat(result.get(0).getStatus()).isEqualTo("EXPIRED");
        assertThat(concertSeat.getStatus()).isEqualTo("AVAILABLE");
    }

}