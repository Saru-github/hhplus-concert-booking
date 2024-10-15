package hhplus.booking.config;

import hhplus.booking.app.concert.domain.entity.Concert;
import hhplus.booking.app.concert.domain.entity.ConcertSchedule;
import hhplus.booking.app.concert.domain.entity.ConcertSeat;
import hhplus.booking.app.concert.infra.jpa.ConcertJpaRepository;
import hhplus.booking.app.concert.infra.jpa.ConcertScheduleJpaRepository;
import hhplus.booking.app.concert.infra.jpa.ConcertSeatJpaRepository;
import hhplus.booking.app.queue.domain.entity.Queue;
import hhplus.booking.app.queue.infra.jpa.QueueJpaRepository;
import hhplus.booking.app.user.domain.entity.User;
import hhplus.booking.app.user.infra.jpa.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class DataInit implements ApplicationRunner {

    private final ConcertJpaRepository concertJpaRepository;
    private final ConcertScheduleJpaRepository concertScheduleJpaRepository;
    private final ConcertSeatJpaRepository concertSeatJpaRepository;
    private final UserJpaRepository userJpaRepository;
    private final QueueJpaRepository queueJpaRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        for (long i = 0;  i < 10; i++) {
            queueJpaRepository.save(new Queue(UUID.randomUUID().toString(), "PROCESSING", null));
        }

        for (int i = 10;  i < 50; i++) {
            queueJpaRepository.save(Queue.from(UUID.randomUUID().toString()));
        }

        for (long scheduleId = 1L; scheduleId <= 9L; scheduleId++) {
            for (long seatNumber = 1; seatNumber <= 50; seatNumber++) {
                concertSeatJpaRepository.save(new ConcertSeat(null, scheduleId, seatNumber, 50000L));
            }
        }

        userJpaRepository.save(User.of("유저1"));
        userJpaRepository.save(new User(2L, "유저2", 100000L));
        userJpaRepository.save(new User(3L, "유저3", 100000L));

        concertJpaRepository.save(new Concert(1L, "IU 콘서트"));
        concertJpaRepository.save(new Concert(2L, "성시경 콘서트"));
        concertJpaRepository.save(new Concert(3L, "에미넴 콘서트"));

        concertScheduleJpaRepository.save(new ConcertSchedule(1L, 1L, LocalDate.now().plusDays(1)));
        concertScheduleJpaRepository.save(new ConcertSchedule(2L, 1L, LocalDate.now().plusDays(2)));
        concertScheduleJpaRepository.save(new ConcertSchedule(3L, 1L, LocalDate.now().plusDays(3)));
        concertScheduleJpaRepository.save(new ConcertSchedule(4L, 2L, LocalDate.now().plusDays(1)));
        concertScheduleJpaRepository.save(new ConcertSchedule(5L, 2L, LocalDate.now().plusDays(2)));
        concertScheduleJpaRepository.save(new ConcertSchedule(6L, 2L, LocalDate.now().plusDays(3)));
        concertScheduleJpaRepository.save(new ConcertSchedule(7L, 3L, LocalDate.now().plusDays(1)));
        concertScheduleJpaRepository.save(new ConcertSchedule(8L, 3L, LocalDate.now().plusDays(2)));
        concertScheduleJpaRepository.save(new ConcertSchedule(9L, 3L, LocalDate.now().plusDays(3)));

        // 각 콘서트 스케줄마다 50개의 좌석을 생성
        for (long scheduleId = 1L; scheduleId <= 9L; scheduleId++) {
            for (long seatNumber = 1; seatNumber <= 50; seatNumber++) {
                concertSeatJpaRepository.save(new ConcertSeat(null, scheduleId, seatNumber, 50000L));
            }
        }
    }
}
