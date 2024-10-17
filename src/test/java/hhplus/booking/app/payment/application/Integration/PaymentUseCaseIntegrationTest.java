package hhplus.booking.app.payment.application.Integration;

import hhplus.booking.app.concert.application.ConcertService;
import hhplus.booking.app.concert.application.dto.ConcertBookingInfo;
import hhplus.booking.app.payment.application.PaymentUseCase;
import hhplus.booking.app.payment.application.dto.PaymentInfo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class PaymentUseCaseIntegrationTest {

    @Autowired
    private PaymentUseCase paymentUseCase;

    @Autowired
    private ConcertService concertService;

    @Test
    @DisplayName("[성공]결제 usecase 통합 테스트 ")
    void successTestPaymentUseCase() throws Exception {

        // Given
        concertService.bookConcertSeat(new ConcertBookingInfo.Input(1L, 11L));

        PaymentInfo.Input input = new PaymentInfo.Input(2L, "TEST_UUID_TOKEN");

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
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("예약이 불가능한 좌석 입니다.");

    }

    @Test
    @DisplayName("[실패] 예약 처리 불가능 (상태값 검증 실패)")
    void failTestPaymentUseCaseWith() throws Exception {

        // given
        PaymentInfo.Input input = new PaymentInfo.Input(1L, "TEST_UUID_TOKEN");

        // when & then
        assertThatThrownBy(() -> paymentUseCase.processPayment(input))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("결제가 불가능한 예약입니다.");

    }
}
