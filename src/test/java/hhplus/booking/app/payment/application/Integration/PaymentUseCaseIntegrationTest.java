package hhplus.booking.app.payment.application.Integration;

import hhplus.booking.app.concert.application.ConcertService;
import hhplus.booking.app.concert.application.dto.ConcertBookingInfo;
import hhplus.booking.app.payment.application.PaymentUseCase;
import hhplus.booking.app.payment.application.dto.PaymentInfo;
import hhplus.booking.config.exception.BusinessException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class PaymentUseCaseIntegrationTest {

    @Autowired
    private PaymentUseCase paymentUseCase;

    @Autowired
    private ConcertService concertService;

    @Test
    @DisplayName("[성공]결제 usecase 통합 테스트 ")
    void successTestPaymentUseCase() throws Exception {

        // Given
        concertService.bookConcertSeat(new ConcertBookingInfo.Input(1L, 12L));

        PaymentInfo.Input input = new PaymentInfo.Input(3L, "Bearer TEST_UUID_TOKEN");

        // When
        PaymentInfo.Output result = paymentUseCase.processPayment(input);

        // Then
        assertThat(result.paymentId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("[실패] 예약 불가능 좌석")
    void failTestPaymentUseCaseWithBookedSeat() throws Exception {

        // when & then
        assertThatThrownBy(() -> concertService.bookConcertSeat(new ConcertBookingInfo.Input(1L, 1L)))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("해당 좌석이 이미 예약되었습니다.");

    }

    @Test
    @DisplayName("[실패] 예약 처리 불가능 (상태값 검증 실패)")
    void failTestPaymentUseCaseWith() throws Exception {

        // given
        PaymentInfo.Input input = new PaymentInfo.Input(2L, "Bearer TEST_UUID_TOKEN");

        // when & then
        assertThatThrownBy(() -> paymentUseCase.processPayment(input))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("결제가 불가능한 예약입니다.");

    }
}
