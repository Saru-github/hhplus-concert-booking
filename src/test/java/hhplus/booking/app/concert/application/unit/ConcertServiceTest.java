package hhplus.booking.app.concert.application.unit;

import hhplus.booking.app.concert.application.ConcertService;
import hhplus.booking.app.concert.application.dto.ConcertBookingInfo;
import hhplus.booking.app.concert.application.dto.ConcertScheduleInfo;
import hhplus.booking.app.concert.application.dto.ConcertSeatInfo;
import hhplus.booking.app.concert.domain.entity.Concert;
import hhplus.booking.app.concert.domain.entity.ConcertBooking;
import hhplus.booking.app.concert.domain.entity.ConcertSchedule;
import hhplus.booking.app.concert.domain.entity.ConcertSeat;
import hhplus.booking.app.concert.domain.repository.ConcertRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.BDDMockito.*;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ConcertServiceTest {

    @Mock
    private ConcertRepository concertRepository;

    @InjectMocks
    private ConcertService concertService;

    Concert concert;
    ConcertSchedule concertSchedule;
    ConcertSeat concertSeat;
    ConcertBooking concertBooking;

    @BeforeEach
    void setUp() {
        concert = new Concert(1L, "아이유 콘서트");
        concertSchedule = new ConcertSchedule(1L,1L, LocalDate.now());
        concertSeat = new ConcertSeat(1L,1L, 1L, 10000L, "AVAILABLE");
        concertBooking = new ConcertBooking(1L, 1L, 1L, "BOOKED", LocalDateTime.now().plusMinutes(5));
    }

    @Test
    @DisplayName("[성공] 1L 콘서트 고유번호 조회시, 콘서트 일정 정보 반환")
    void successTestGetConcertScheduleInfos() {

        // given
        ConcertScheduleInfo.Input input = new ConcertScheduleInfo.Input(1L);
        given(concertRepository.getConcertSchedules(1L)).willReturn(List.of(concertSchedule));

        // when
        List<ConcertScheduleInfo.Output> result = concertService.getConcertScheduleInfos(input);

        // then
        assertThat(result.get(0).concertScheduleId()).isEqualTo(1L);
        assertThat(result.get(0).concertDate()).isEqualTo(LocalDate.now());
    }

    @Test
    @DisplayName("[성공] 1L 콘서트일정 고유번호 조회시, 예약가능 콘서트 좌석 정보 반환")
    void successTestGetConcertAvailableSeatsInfos() {

        // given
        ConcertSeatInfo.Input input = new ConcertSeatInfo.Input(1L);
        given(concertRepository.getAvailableConcertSeats(1L)).willReturn(List.of(concertSeat));

        // when
        List<ConcertSeatInfo.Output> result = concertService.getAvailableConcertSeatInfos(input);

        // then
        assertThat(result.get(0).concertSeatId()).isEqualTo(1L);
        assertThat(result.get(0).concertSeatId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("[성공] 예약가능한 1L 콘서트 일정 고유번호, 1L 콘서트 좌석 고유번호로 예약 성공 후 정보 반환")
    void successTestBookConcertSeat() {

        // given
        long userId = 1L;
        ConcertBookingInfo.Input input = new ConcertBookingInfo.Input(userId, concertSeat.getConcertSeatId());
        given(concertRepository.getConcertSeat(1L)).willReturn(concertSeat);
        given(concertRepository.bookConcertSeat(1L, 1L)).willReturn(concertBooking);

        // when
        ConcertBookingInfo.Output result = concertService.bookConcertSeat(input);

        // then
        assertThat(result.concertBookingId()).isEqualTo(1L);
    }

}