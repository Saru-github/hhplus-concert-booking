package hhplus.booking.app.queue.application.Integration;

import hhplus.booking.app.queue.application.QueueService;
import hhplus.booking.app.queue.application.dto.QueueInfo;
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
class ConcurrentQueueServiceIntegrationTest {

    @Autowired
    private QueueService queueService;


    @Test
    @DisplayName("40명이 동시에 대기열 등록시 모두 정상 등록 되어야 함")
    void testConcurrentQueueInfo() throws Exception {

        // Given
        List<CompletableFuture<QueueInfo.Output>> futures = new ArrayList<>();
        AtomicLong exceptionCount = new AtomicLong(0L);
        List<String> tokenList = new ArrayList<>();

        // 초기 Queue 데이터 설정
        for (int i = 1; i <= 40; i++) {
            QueueInfo.Output result = queueService.getQueueInfo(new QueueInfo.Input(null));
            tokenList.add(result.tokenValue());
        }

        // When
        for (int i = 0; i < 40; i++) {
            int finalI = i;
            CompletableFuture<QueueInfo.Output> future = CompletableFuture.supplyAsync(() -> {
                try {
                    QueueInfo.Input input = new QueueInfo.Input(tokenList.get(finalI));
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
        log.info(results.toString());
        // 추가 검증: 순서가 올바르게 보장되었는지 확인
    }
}
