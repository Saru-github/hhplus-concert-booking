package hhplus.booking.app.user.application.Integration;

import hhplus.booking.app.user.application.UserService;
import hhplus.booking.app.user.application.dto.UserPointInfo;
import hhplus.booking.app.user.domain.entity.User;
import hhplus.booking.app.user.infra.jpa.UserJpaRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest
class ConcurrentUserIntegrationTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserJpaRepository userJpaRepository;


    @Test
    @DisplayName("[성공] 새로생성한 4번 유저의 10번의 10000포인트 요청시, 100000만 포인트 올바르게 충전")
    void testPaymentUseCaseTest() throws Exception {

        // Given

        userJpaRepository.save(User.of("대영"));
        UserPointInfo.Output beforeUserPoint = userService.getUserPoints(new UserPointInfo.Input(4L, null));

        List<CompletableFuture<UserPointInfo.Output>> futures = new ArrayList<>();

        // When
        for (int i = 0; i < 10; i++) {
            CompletableFuture<UserPointInfo.Output> future = CompletableFuture.supplyAsync(() -> {
                return userService.chargeUserPoints(new UserPointInfo.Input(4L, 10000L));
            });
            futures.add(future);
        }

        CompletableFuture<Void> allOf = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
        allOf.get();

        // Then
        List<UserPointInfo.Output> results = new ArrayList<>();
        for (CompletableFuture<UserPointInfo.Output> future : futures) {
            UserPointInfo.Output output = future.join();
            if (output != null) {
                results.add(output);
            }
        }

        UserPointInfo.Output afterUserPoint = userService.getUserPoints(new UserPointInfo.Input(4L, null));

        // 결과 확인
        assertThat(beforeUserPoint.balance()).isEqualTo(0L);
        assertThat(afterUserPoint.balance()).isEqualTo(100000L);
    }
}
