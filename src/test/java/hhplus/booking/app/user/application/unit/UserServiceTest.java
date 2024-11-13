package hhplus.booking.app.user.application.unit;

import hhplus.booking.app.user.application.UserService;
import hhplus.booking.app.user.application.dto.UserPointInfo;
import hhplus.booking.app.user.domain.entity.User;
import hhplus.booking.app.user.domain.repository.UserRepository;
import hhplus.booking.config.exception.BusinessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.BDDMockito.*;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserService userService;

    User user;

    @BeforeEach
    void setUp() {
        user = new User(1L, "유저1", 100000L);
    }

    @Test
    @DisplayName("[성공] 유저 고유 번호 1의 포인트 잔액을 100000을 반환")
    void successTestGetUserPoints() {

        // given
        UserPointInfo.Input input = new UserPointInfo.Input(1L, null);
        given(userRepository.getUser(1L)).willReturn(user);

        // when
        UserPointInfo.Output result = userService.getUserPoints(input);

        // then
        assertThat(result.userId()).isEqualTo(1L);
        assertThat(result.balance()).isEqualTo(100000L);

    }

    @Test
    @DisplayName("[성공] 유저 고유 번호 1의 포인트 잔액을 10000 만큼 충전 후 잔액 110000 반환")
    void successTestChargeUserPoints() {

        // given
        UserPointInfo.Input input = new UserPointInfo.Input(1L, 10000L);
        given(userRepository.findUserWithLockById(1L)).willReturn(user);

        // when
        UserPointInfo.Output result = userService.chargeUserPoints(input);

        // then
        assertThat(result.userId()).isEqualTo(1L);
        assertThat(result.balance()).isEqualTo(110000L);
    }

    @Test
    @DisplayName("[성공] 유저 고유 번호 1의 포인트 잔액을 10000 만큼 사용 후 잔액 90000 반환")
    void successTestUseUserPoints() {

        // given
        UserPointInfo.Input input = new UserPointInfo.Input(1L, 10000L);
        given(userRepository.findUserWithLockById(1L)).willReturn(user);

        // when
        UserPointInfo.Output result = userService.useUserPoints(input);

        // then
        assertThat(result.userId()).isEqualTo(1L);
        assertThat(result.balance()).isEqualTo(90000);
    }

    @Test
    @DisplayName("[실패] 유저 고유 번호 1의 요청 포인트가 0 이하일 때 에러 반환")
    void failTestChargeUserPointWhenAmountUnderZero() {

        // when & then
        assertThatThrownBy(() -> new UserPointInfo.Input(1L, 0L))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("요청 포인트는 0 이하 일 수 없습니다.");
    }


    @Test
    @DisplayName("[실패] 유저 고유 번호 1의 포인트 잔액을 1000001 만큼 사용 요청시 잔액 부족으로 에러 반환")
    void failTestUseUserPointsWhenBalanceLessThenAmount() {

        // given
        UserPointInfo.Input input = new UserPointInfo.Input(1L, 1000001L);
        given(userRepository.findUserWithLockById(1L)).willReturn(user);

        // when & then
        assertThatThrownBy(() -> userService.useUserPoints(input))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("남은 잔액이 부족합니다.");

    }
}