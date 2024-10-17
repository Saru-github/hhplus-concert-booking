package hhplus.booking.app.payment.application.Integration;

import hhplus.booking.app.queue.application.QueueService;
import hhplus.booking.app.queue.application.dto.QueueInfo;
import hhplus.booking.app.queue.domain.entity.Queue;
import hhplus.booking.app.queue.infra.jpa.QueueJpaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicLong;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class PaymentUseCaseTest {

    @Autowired
    private QueueService queueService;

    @Autowired
    private QueueJpaRepository queueRepository;

    @Test
    @DisplayName("40명이 동시에 대기열 조회 시 순서 보장 테스트")
    void testPaymentUseCaseTest() throws Exception {

        // Given
        List<CompletableFuture<QueueInfo.Output>> futures = new ArrayList<>();
        AtomicLong exceptionCount = new AtomicLong(0L);

        // 초기 Queue 데이터 설정
        for (int i = 1; i <= 40; i++) {
            Queue queue = new Queue((long) i, "tokenValue" + i, "WAITING", LocalDateTime.now().plusMinutes(1), LocalDateTime.now(), LocalDateTime.now());
            queueRepository.save(queue);
        }

        // When
        for (int i = 1; i <= 40; i++) {
            int finalI = i;
            CompletableFuture<QueueInfo.Output> future = CompletableFuture.supplyAsync(() -> {
                try {
                    QueueInfo.Input input = new QueueInfo.Input("Bearer tokenValue" + finalI);
                    return queueService.getQueueInfo(input);
                } catch (Exception e) {
                    exceptionCount.getAndIncrement();
                    return null;
                }
            });
            futures.add(future);
        }

        // 모든 CompletableFuture가 완료될 때까지 대기
        CompletableFuture<Void> allOf = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
        allOf.get();

        // Then
        List<QueueInfo.Output> results = new ArrayList<>();
        for (CompletableFuture<QueueInfo.Output> future : futures) {
            QueueInfo.Output output = future.join();
            if (output != null) {
                results.add(output);
            }
        }

        // 결과 확인
        assertThat(results).hasSize(40); // 총 40개의 결과가 있어야 함
        results.forEach(System.out::println);
        // 추가 검증: 순서가 올바르게 보장되었는지 확인
        for (int i = 0; i < results.size(); i++) {
            assertThat(results.get(i).rank()).isEqualTo(i + 1); // rank가 0부터 시작해야 함
        }
    }
}
