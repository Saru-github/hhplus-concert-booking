package hhplus.booking.app.concert.application;

import hhplus.booking.app.concert.application.dto.ConcertScheduleInfo;
import hhplus.booking.app.concert.application.dto.ConcertSeatInfo;
import hhplus.booking.app.concert.domain.entity.Concert;
import hhplus.booking.app.concert.domain.entity.ConcertSchedule;
import hhplus.booking.app.concert.domain.entity.ConcertSeat;
import hhplus.booking.app.concert.domain.repository.ConcertRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConcertService {

    private final ConcertRepository concertRepository;

    public List<ConcertScheduleInfo.Output> getConcertScheduleInfo(ConcertScheduleInfo.Input input) {
        List<ConcertSchedule> concertSchedules = concertRepository.getConcertSchedule(input.concertId());
        return concertSchedules.stream()
                .map(schedule -> new ConcertScheduleInfo.Output(schedule.getConcertScheduleId(), schedule.getConcertDate()))
                .toList();
    }

    public List<ConcertSeatInfo.Output> getConcertSeatInfo(ConcertSeatInfo.Input input) {
        List<ConcertSeat> concertSeats = concertRepository.getConcertSeat(input.concertScheduleId());
        return concertSeats.stream()
                .map(ConcertSeatInfo.Output::new)
                .toList();
    }
}
