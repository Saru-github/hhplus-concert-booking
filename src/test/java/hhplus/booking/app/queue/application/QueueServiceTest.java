package hhplus.booking.app.queue.application;

import hhplus.booking.app.queue.application.dto.QueueInfo;
import hhplus.booking.app.queue.domain.repository.QueueRepository;
import hhplus.booking.config.exception.BusinessException;
import hhplus.booking.config.exception.ErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class QueueServiceTest {

    @Mock
    private QueueRepository queueRepository;

    @InjectMocks
    private QueueService queueService;

    private String tokenValue;

    @BeforeEach
    void setUp() {
        tokenValue = UUID.randomUUID().toString();
    }

    @Test
    @DisplayName("[성공] 토큰 없을 때, 토큰 발급 후 대기열 순번 리턴")
    void successTestGetQueueInfoWithNoTokenValue() {
        // given
        given(queueRepository.registerQueue()).willReturn(tokenValue);
        given(queueRepository.getQueueRank(tokenValue)).willReturn(1L);

        // when
        QueueInfo.Output result = queueService.getQueueInfo(new QueueInfo.Input(null));

        // then
        assertThat(result.tokenValue()).isEqualTo(tokenValue);
        assertThat(result.rank()).isEqualTo(1L);
        assertThat(result.status()).isEqualTo("WAITING");
    }

    @Test
    @DisplayName("[실패] 잘못된 토큰 정보 입력했을 때, BusinessException 발생")
    void failureTestGetQueueInfoWithInvalidTokenValue() {
        String invalidToken = "Bearer invalidTokenValue";

        // given: 잘못된 토큰 값에 대해 예외를 발생하도록 설정
        given(queueRepository.getQueueRank(invalidToken.substring(7))).willReturn(null);

        // when & then: getQueueInfo 호출 시 예외가 발생하는지 검증
        assertThatThrownBy(() -> queueService.getQueueInfo(new QueueInfo.Input(invalidToken)))
            .isInstanceOf(BusinessException.class)
            .hasMessageContaining(ErrorCode.INVALID_TOKEN.getMessage());
    }


    @Test
    @DisplayName("[성공] 올바른 토큰 정보 입력했을 때, 토큰 검증 후 대기열 순번 리턴")
    void successTestGetQueueInfoWithTokenValue() {
        // given
        given(queueRepository.getQueueRank(tokenValue)).willReturn(5L);

        // when
        QueueInfo.Output result = queueService.getQueueInfo(new QueueInfo.Input("Bearer " + tokenValue));

        // then
        assertThat(result.tokenValue()).isEqualTo(tokenValue);
        assertThat(result.rank()).isEqualTo(5L);
        assertThat(result.status()).isEqualTo("WAITING");
    }

    @Test
    @DisplayName("[성공] 처리 중인 토큰이 있을 때, 상태 'PROCESSING' 리턴")
    void successTestGetQueueInfoWithProcessingToken() {
        // given
        given(queueRepository.getQueueRank(tokenValue)).willReturn(0L);

        // when
        QueueInfo.Output result = queueService.getQueueInfo(new QueueInfo.Input("Bearer " + tokenValue));

        // then
        assertThat(result.tokenValue()).isEqualTo(tokenValue);
        assertThat(result.rank()).isEqualTo(0L);
        assertThat(result.status()).isEqualTo("PROCESSING");
    }
}

