package hhplus.booking.app.concert.application;

import hhplus.booking.app.concert.application.dto.ConcertBookingInfo;
import hhplus.booking.app.concert.application.dto.ConcertScheduleInfo;
import hhplus.booking.app.concert.application.dto.ConcertSeatInfo;
import hhplus.booking.app.concert.domain.entity.Concert;
import hhplus.booking.app.concert.domain.entity.ConcertBooking;
import hhplus.booking.app.concert.domain.entity.ConcertSchedule;
import hhplus.booking.app.concert.domain.entity.ConcertSeat;
import hhplus.booking.app.concert.domain.repository.ConcertRepository;
import hhplus.booking.app.concert.infra.jpa.ConcertSeatJpaRepository;
import hhplus.booking.app.user.domain.entity.User;
import hhplus.booking.app.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConcertService {

    private final ConcertRepository concertRepository;
    private final UserRepository userRepository;

    public List<ConcertScheduleInfo.Output> getConcertScheduleInfos(ConcertScheduleInfo.Input input) {
        List<ConcertSchedule> concertSchedules = concertRepository.getConcertSchedules(input.concertId());
        return concertSchedules.stream()
                .map(schedule -> new ConcertScheduleInfo.Output(schedule.getConcertScheduleId(), schedule.getConcertDate()))
                .toList();
    }

    public List<ConcertSeatInfo.Output> getAvailableConcertSeatInfos(ConcertSeatInfo.Input input) {
        List<ConcertSeat> concertSeats = concertRepository.getAvailableConcertSeats(input.concertScheduleId());
        return concertSeats.stream()
                .map(ConcertSeatInfo.Output::new)
                .toList();
    }

    // TODO: 예약완료 시 좌석 점유 및 대기열 상태 임시예약 상태로 변경(status, expired 사용) (트랜잭션 적용 해야함)
    @Transactional
    public ConcertBookingInfo.Output bookConcertSeat(ConcertBookingInfo.Input input) {
        try {
            ConcertSeat concertSeat = concertRepository.getConcertSeat(input.concertSeatId());
            concertSeat.validAvailableSeat(); // 유효성 검증
            ConcertBooking concertBooking = concertRepository.bookConcertSeat(input.userId(), input.concertSeatId());
            concertSeat.updateSeatStatusToBooked(); // 상태 업데이트
            return new ConcertBookingInfo.Output(concertBooking);
        } catch (OptimisticLockingFailureException e) {
            throw new IllegalStateException("해당 좌석이 이미 예약되었습니다. 다시 시도해 주세요.");
        }
    }
}
