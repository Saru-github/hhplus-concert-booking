package hhplus.booking.config;

import hhplus.booking.app.concert.domain.entity.Concert;
import hhplus.booking.app.concert.domain.entity.ConcertBooking;
import hhplus.booking.app.concert.domain.entity.ConcertSchedule;
import hhplus.booking.app.concert.domain.entity.ConcertSeat;
import hhplus.booking.app.concert.infra.jpa.ConcertBookingJpaRepository;
import hhplus.booking.app.concert.infra.jpa.ConcertJpaRepository;
import hhplus.booking.app.concert.infra.jpa.ConcertScheduleJpaRepository;
import hhplus.booking.app.concert.infra.jpa.ConcertSeatJpaRepository;
import hhplus.booking.app.queue.domain.entity.Queue;
import hhplus.booking.app.queue.infra.RedisQueueRepository;
import hhplus.booking.app.queue.infra.jpa.QueueJpaRepository;
import hhplus.booking.app.user.domain.entity.User;
import hhplus.booking.app.user.infra.jpa.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class DataInit implements ApplicationRunner {

    private final ConcertJpaRepository concertJpaRepository;
    private final ConcertScheduleJpaRepository concertScheduleJpaRepository;
    private final ConcertSeatJpaRepository concertSeatJpaRepository;
    private final UserJpaRepository userJpaRepository;
    private final QueueJpaRepository queueJpaRepository;
    private final ConcertBookingJpaRepository concertBookingJpaRepository;

    private final RedisQueueRepository redisQueueRepository;
    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        runningRedisData();

    }

    public void runningJpaData() {
        // queueId 1~10: PROCESSING 대기열 10개 생성
        for (long i = 0;  i < 10; i++) {
            queueJpaRepository.save(new Queue(UUID.randomUUID().toString(), "PROCESSING", null));
        }

        // queueId 10~50: WAITING 대기열 40개 생성
        for (int i = 10;  i < 50; i++) {
            queueJpaRepository.save(Queue.from(UUID.randomUUID().toString()));
        }

        // queueID 51: TEST_UUID_TOKEN를 tokenValue로 가진 WAITING 대기열 1개 생성
        queueJpaRepository.save(new Queue("TEST_UUID_TOKEN", "WAITING", null));

        // userId 1~3: 10만 포인트를 가진 유저 생성
        userJpaRepository.save(new User(null, "유저1", 10000000L));
        userJpaRepository.save(new User(null, "유저2", 100000L));
        userJpaRepository.save(new User(null, "유저3", 100000L));

        // concertID 1~3: 콘서트 3개 생성
        concertJpaRepository.save(new Concert(1L, "IU 콘서트"));
        concertJpaRepository.save(new Concert(2L, "성시경 콘서트"));
        concertJpaRepository.save(new Concert(3L, "에미넴 콘서트"));

        // concertScheduleId 1~9: 각 콘서트 1~3의 오늘, 내일, 모레 일정 생성
        concertScheduleJpaRepository.save(new ConcertSchedule(1L, 1L, LocalDate.now().plusDays(1)));
        concertScheduleJpaRepository.save(new ConcertSchedule(2L, 1L, LocalDate.now().plusDays(2)));
        concertScheduleJpaRepository.save(new ConcertSchedule(3L, 1L, LocalDate.now().plusDays(3)));
        concertScheduleJpaRepository.save(new ConcertSchedule(4L, 2L, LocalDate.now().plusDays(1)));
        concertScheduleJpaRepository.save(new ConcertSchedule(5L, 2L, LocalDate.now().plusDays(2)));
        concertScheduleJpaRepository.save(new ConcertSchedule(6L, 2L, LocalDate.now().plusDays(3)));
        concertScheduleJpaRepository.save(new ConcertSchedule(7L, 3L, LocalDate.now().plusDays(1)));
        concertScheduleJpaRepository.save(new ConcertSchedule(8L, 3L, LocalDate.now().plusDays(2)));
        concertScheduleJpaRepository.save(new ConcertSchedule(9L, 3L, LocalDate.now().plusDays(3)));

        // concertSeatId 1~50: 각 콘서트 스케줄마다 50개의 좌석을 생성, 좌석번호 10번까지는 BOOKED, 나머지는 AVAILABLE 상태
        for (long scheduleId = 1L; scheduleId <= 9L; scheduleId++) {
            for (long seatNumber = 1; seatNumber <= 50; seatNumber++) {
                String status = (seatNumber <= 10) ? "BOOKED" : "AVAILABLE";
                concertSeatJpaRepository.save(new ConcertSeat(null, scheduleId, seatNumber, 50000L, status));
            }
        }

        // concertBookingID 1: 1번유저, 2번좌석 을 가진 예약 정보 생성
        concertBookingJpaRepository.save(new ConcertBooking(null,1L, 11L, "BOOKED", LocalDateTime.now().plusMinutes(5)));

        // concertBookingID 2: 1번유저, 1번좌석 을 가진 예약 정보 생성
        concertBookingJpaRepository.save(new ConcertBooking(null,1L, 1L, "COMPLETED", LocalDateTime.now()));
    }

    public void runningRedisData() {
        // queueId 1~10: PROCESSING 대기열 10개 생성
        for (long i = 0;  i < 10; i++) {
            redisQueueRepository.registerQueue();
        }

        // queueId 10~50: WAITING 대기열 40개 생성
        for (int i = 10;  i < 109; i++) {

            LocalDateTime nowKST = LocalDateTime.now(ZoneOffset.ofHours(9));  // 한국 시간(KST) 기준
            long timestamp = nowKST.toEpochSecond(ZoneOffset.ofHours(9));  // 시간(초 단위)
            long expirationTimestamp = timestamp + 60;

            // Redis에 대기 중인 큐를 추가. 점수는 현재 시간으로 설정.
            String queueTokenValue = UUID.randomUUID().toString();
            redisTemplate.opsForZSet().add("queue:waiting", queueTokenValue, timestamp);
            queueTokenValue = UUID.randomUUID().toString();
            redisTemplate.opsForValue().set("processingToken: " +  queueTokenValue, queueTokenValue, expirationTimestamp);

        }

        // queueID 51: TEST_UUID_TOKEN를 tokenValue로 가진 WAITING 대기열 1개 생성
        LocalDateTime nowKST = LocalDateTime.now(ZoneOffset.ofHours(9));  // 한국 시간(KST) 기준
        long timestamp = nowKST.toEpochSecond(ZoneOffset.ofHours(9));  // 시간(초 단위)

        long expirationTimestamp = timestamp + 60;

        // Redis에 대기 중인 큐를 추가. 점수는 현재 시간으로 설정.
        redisTemplate.opsForZSet().add("queue:waiting", "TEST_WAITING_TOKEN", timestamp);
        redisTemplate.opsForValue().set("processingToken: " + "TEST_PROCESSING_TOKEN", "TEST_PROCESSING_TOKEN", expirationTimestamp);
//
//        // userId 1~3: 10만 포인트를 가진 유저 생성
//        userJpaRepository.save(new User(null, "유저1", 10000000L));
//        userJpaRepository.save(new User(null, "유저2", 100000L));
//        userJpaRepository.save(new User(null, "유저3", 100000L));
//
//        // concertID 1~3: 콘서트 3개 생성
//        concertJpaRepository.save(new Concert(1L, "IU 콘서트"));
//        concertJpaRepository.save(new Concert(2L, "성시경 콘서트"));
//        concertJpaRepository.save(new Concert(3L, "에미넴 콘서트"));
//
//        // concertScheduleId 1~9: 각 콘서트 1~3의 오늘, 내일, 모레 일정 생성
//        concertScheduleJpaRepository.save(new ConcertSchedule(1L, 1L, LocalDate.now().plusDays(1)));
//        concertScheduleJpaRepository.save(new ConcertSchedule(2L, 1L, LocalDate.now().plusDays(2)));
//        concertScheduleJpaRepository.save(new ConcertSchedule(3L, 1L, LocalDate.now().plusDays(3)));
//        concertScheduleJpaRepository.save(new ConcertSchedule(4L, 2L, LocalDate.now().plusDays(1)));
//        concertScheduleJpaRepository.save(new ConcertSchedule(5L, 2L, LocalDate.now().plusDays(2)));
//        concertScheduleJpaRepository.save(new ConcertSchedule(6L, 2L, LocalDate.now().plusDays(3)));
//        concertScheduleJpaRepository.save(new ConcertSchedule(7L, 3L, LocalDate.now().plusDays(1)));
//        concertScheduleJpaRepository.save(new ConcertSchedule(8L, 3L, LocalDate.now().plusDays(2)));
//        concertScheduleJpaRepository.save(new ConcertSchedule(9L, 3L, LocalDate.now().plusDays(3)));
//
//        // concertSeatId 1~50: 각 콘서트 스케줄마다 50개의 좌석을 생성, 좌석번호 10번까지는 BOOKED, 나머지는 AVAILABLE 상태
//        for (long scheduleId = 1L; scheduleId <= 9L; scheduleId++) {
//            for (long seatNumber = 1; seatNumber <= 50; seatNumber++) {
//                String status = (seatNumber <= 10) ? "BOOKED" : "AVAILABLE";
//                concertSeatJpaRepository.save(new ConcertSeat(null, scheduleId, seatNumber, 50000L, status));
//            }
//        }
//
//        // concertBookingID 1: 1번유저, 2번좌석 을 가진 예약 정보 생성
//        concertBookingJpaRepository.save(new ConcertBooking(null,1L, 11L, "BOOKED", LocalDateTime.now().plusMinutes(5)));
//
//        // concertBookingID 2: 1번유저, 1번좌석 을 가진 예약 정보 생성
//        concertBookingJpaRepository.save(new ConcertBooking(null,1L, 1L, "COMPLETED", LocalDateTime.now()));
    }
}
