package hhplus.booking.app.concert.application;

import hhplus.booking.app.concert.domain.entity.ConcertBooking;
import hhplus.booking.app.concert.domain.repository.ConcertRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConcertSchedulerService {

    private final ConcertRepository concertRepository;

    // 좌석 임시 예약 -> 예약해제, 임시 예약 만료 등 스케쥴러
    @Transactional
    public List<ConcertBooking> expiredConcertBookingScheduler() {

        List<ConcertBooking> expiredConcertBookings =  concertRepository.getExpiredBookings();

        for (ConcertBooking expiredConcertBooking : expiredConcertBookings) {
            expiredConcertBooking.expiredBookingStatus();
            concertRepository.getConcertSeat(expiredConcertBooking.getConcertSeatId()).updateSeatStatusToAvailable();

            log.info("임시예약 만료된 예약 수: {}", expiredConcertBookings.size());

        }

        return expiredConcertBookings;
    }

    @Transactional
    @Scheduled(cron = "0 */1 * * * *", zone = "Asia/Seoul")
    public void execute() {
        expiredConcertBookingScheduler();
    }
}
