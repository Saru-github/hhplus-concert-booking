package hhplus.booking.app.concert.application.Integration;

import hhplus.booking.app.concert.application.ConcertService;
import hhplus.booking.app.concert.application.dto.ConcertBookingInfo;
import hhplus.booking.app.concert.domain.entity.ConcertBooking;
import hhplus.booking.app.concert.infra.jpa.ConcertBookingJpaRepository;
import hhplus.booking.app.concert.infra.jpa.ConcertSeatJpaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicLong;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ConcurrentConcertBookingIntegrationTest {

    @Autowired
    private ConcertService concertService;

    @Autowired
    private ConcertBookingJpaRepository concertBookingJpaRepository;

    @Test
    @DisplayName("10명이 동시에 좌석 신청 1번만 예약 보장 테스트")
    void testPaymentUseCaseTest() throws Exception {

        // Given
        List<CompletableFuture<ConcertBookingInfo.Output>> futures = new ArrayList<>();
        AtomicLong atomicExceptionCount = new AtomicLong(0L);
        AtomicLong atomicSuccessUserId = new AtomicLong();

        // When
        for (int i = 11; i <= 20; i++) {
            long finalI = i;
            CompletableFuture<ConcertBookingInfo.Output> future = CompletableFuture.supplyAsync(() -> {
                try {
                    ConcertBookingInfo.Input input = new ConcertBookingInfo.Input(finalI, 11L);
                    ConcertBookingInfo.Output output =  concertService.bookConcertSeat(input);
                    atomicSuccessUserId.set(finalI);
                    return output;

                } catch (Exception e) {
                    atomicExceptionCount.getAndIncrement();
                    return null;
                }
            });
            futures.add(future);
        }

        // 모든 CompletableFuture가 완료될 때까지 대기
        CompletableFuture<Void> allOf = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
        allOf.get();

        // Then
        List<ConcertBookingInfo.Output> results = new ArrayList<>();
        for (CompletableFuture<ConcertBookingInfo.Output> future : futures) {
            ConcertBookingInfo.Output output = future.join();
            if (output != null) {
                results.add(output);
            }
        }

        ConcertBooking concertBooking = concertBookingJpaRepository.findById(results.get(0).concertBookingId()).orElseThrow();

        long userId = atomicSuccessUserId.get();
        long exceptionCount = atomicExceptionCount.get();

        // 결과 확인
        assertThat(results).hasSize(1);
        assertThat(exceptionCount).isEqualTo(9);
        assertThat(concertBooking.getUserId()).isEqualTo(userId);
    }
}
