package hhplus.booking.app.payment.application.Integration;

import hhplus.booking.app.concert.infra.jpa.ConcertBookingJpaRepository;
import hhplus.booking.app.payment.application.PaymentUseCase;
import hhplus.booking.app.payment.application.dto.PaymentInfo;
import hhplus.booking.app.payment.infra.jpa.PaymentJpaRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicLong;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest
class ConcurrentPaymentIntegrationTest {

    @Autowired
    private PaymentUseCase paymentUseCase;

    @Test
    @DisplayName("[성공] 예약된 상태인 1번 예약에 관하여 정상적으로 1건만 최종 예약성공")
    void testPaymentUseCaseTest() throws Exception {

        // Given
        List<CompletableFuture<PaymentInfo.Output>> futures = new ArrayList<>();
        AtomicLong atomicExceptionCount = new AtomicLong(0L);

        // When
        for (int i = 0; i < 10; i++) {
            int finalI = i;
            CompletableFuture<PaymentInfo.Output> future = CompletableFuture.supplyAsync(() -> {
                try {
                    log.info("시도: {}", finalI);
                    return paymentUseCase.processPayment(new PaymentInfo.Input(1L, "Bearer TEST_UUID_TOKEN"));
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
        List<PaymentInfo.Output> results = new ArrayList<>();
        for (CompletableFuture<PaymentInfo.Output> future : futures) {
            PaymentInfo.Output output = future.join();
            if (output != null) {
                results.add(output);
            }
        }
        long exceptionCount = atomicExceptionCount.get();

        // 결과 확인
        assertThat(results).hasSize(1);
        assertThat(exceptionCount).isEqualTo(9);
    }
}