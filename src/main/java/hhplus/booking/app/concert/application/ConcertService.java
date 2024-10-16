package hhplus.booking.app.concert.application;

import hhplus.booking.app.concert.application.dto.ConcertBookingInfo;
import hhplus.booking.app.concert.application.dto.ConcertScheduleInfo;
import hhplus.booking.app.concert.application.dto.ConcertSeatInfo;
import hhplus.booking.app.concert.domain.entity.Concert;
import hhplus.booking.app.concert.domain.entity.ConcertBooking;
import hhplus.booking.app.concert.domain.entity.ConcertSchedule;
import hhplus.booking.app.concert.domain.entity.ConcertSeat;
import hhplus.booking.app.concert.domain.repository.ConcertRepository;
import hhplus.booking.app.user.domain.entity.User;
import hhplus.booking.app.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConcertService {

    private final ConcertRepository concertRepository;
    private final UserRepository userRepository;

    public List<ConcertScheduleInfo.Output> getConcertScheduleInfo(ConcertScheduleInfo.Input input) {
        List<ConcertSchedule> concertSchedules = concertRepository.getConcertSchedule(input.concertId());
        return concertSchedules.stream()
                .map(schedule -> new ConcertScheduleInfo.Output(schedule.getConcertScheduleId(), schedule.getConcertDate()))
                .toList();
    }

    // TODO: 임시점유 좌석 where 절 추가
    public List<ConcertSeatInfo.Output> getConcertSeatInfo(ConcertSeatInfo.Input input) {
        List<ConcertSeat> concertSeats = concertRepository.getConcertSeats(input.concertScheduleId());
        return concertSeats.stream()
                .map(ConcertSeatInfo.Output::new)
                .toList();
    }

    // TODO: 예약완료 시 좌석 점유 및 대기열 상태 임시예약 상태로 변경(status, expired 사용)
    public ConcertBookingInfo.Output bookConcertSeat(ConcertBookingInfo.Input input) {
        User user = userRepository.getUser(input.userId());
        ConcertSeat concertSeat = concertRepository.getConcertSeats(input.concertSeatId());
        ConcertBooking concertBooking = concertRepository.bookConcertSeat(user.getUserId(), input.concertSeatId());
        return new ConcertBookingInfo.Output(concertBooking);
    }
}
