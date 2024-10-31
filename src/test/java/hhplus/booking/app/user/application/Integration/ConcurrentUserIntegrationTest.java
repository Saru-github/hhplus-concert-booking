package hhplus.booking.app.user.application.Integration;

import hhplus.booking.app.ExecutionTimeWatcher;
import hhplus.booking.app.user.application.UserService;
import hhplus.booking.app.user.application.dto.UserPointInfo;
import hhplus.booking.app.user.domain.entity.User;
import hhplus.booking.app.user.infra.jpa.UserJpaRepository;
import hhplus.booking.config.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest
@ExtendWith(ExecutionTimeWatcher.class)
class ConcurrentUserIntegrationTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserJpaRepository userJpaRepository;

    @Test
    @DisplayName("[낙관적락_성공] 새로생성한 4번 유저의 5번의 10000포인트 요청시, 단 한번만 성공하고 나머지는 실패")
    void optimisticLockTestUserPointUserTest() throws Exception {
        // Given
        userJpaRepository.save(User.of("대영"));
        UserPointInfo.Output beforeUserPoint = userService.getUserPoints(new UserPointInfo.Input(4L, null));

        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failCount = new AtomicInteger(0);

        List<CompletableFuture<Void>> futures = new ArrayList<>();

        // When
        for (int i = 0; i < 5; i++) {
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                try {
                    userService.chargeUserPoints(new UserPointInfo.Input(4L, 10000L));
                    successCount.incrementAndGet();
                } catch (BusinessException e) {
                    failCount.incrementAndGet();
                } catch (Exception e) {
                    failCount.incrementAndGet();
                }
            });
            futures.add(future);
        }

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

        // Then
        UserPointInfo.Output afterUserPoint = userService.getUserPoints(new UserPointInfo.Input(4L, null));

        assertThat(beforeUserPoint.balance()).isEqualTo(0L);
        assertThat(afterUserPoint.balance()).isEqualTo(10000L);
        assertThat(successCount.get()).isEqualTo(1);
        assertThat(failCount.get()).isEqualTo(4);

        log.info("테스트 시작 전 포인트: {}", beforeUserPoint.balance());
        log.info("테스트 시작 후 포인트: {}", afterUserPoint.balance());
        log.info("성공 횟수: {}", successCount.get());
        log.info("실패 횟수: {}", failCount.get());
    }

}
