package hhplus.booking.app.payment.application;

import hhplus.booking.app.concert.domain.entity.Concert;
import hhplus.booking.app.concert.domain.entity.ConcertBooking;
import hhplus.booking.app.concert.domain.entity.ConcertSchedule;
import hhplus.booking.app.concert.domain.entity.ConcertSeat;
import hhplus.booking.app.concert.domain.repository.ConcertRepository;
import hhplus.booking.app.payment.application.dto.PaymentEventInfo;
import hhplus.booking.app.payment.application.dto.PaymentInfo;
import hhplus.booking.app.payment.domain.entity.OutBox;
import hhplus.booking.app.payment.domain.entity.Payment;
import hhplus.booking.app.payment.domain.event.kafka.PaymentEventPublisher;
import hhplus.booking.app.payment.domain.event.kafka.dto.PaymentSuccessEvent;
import hhplus.booking.app.payment.domain.repository.PaymentRepository;
import hhplus.booking.app.payment.infra.jpa.OutBoxJpaRepository;
import hhplus.booking.app.user.domain.entity.User;
import hhplus.booking.app.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class PaymentUseCase {

    private final UserRepository userRepository;
    private final ConcertRepository concertRepository;
    private final PaymentRepository paymentRepository;
    private final PaymentEventPublisher paymentEventPublisher;
    private final OutBoxJpaRepository outBoxJpaRepository;

    @Transactional
    public PaymentInfo.Output processPayment(PaymentInfo.Input input) {

        ConcertBooking concertBooking = concertRepository.getConcertBooking(input.concertBookingId());
        concertBooking.validConcertBookingStatus();

        ConcertSeat concertSeat = concertRepository.getConcertSeat(concertBooking.getConcertSeatId());
        ConcertSchedule concertSchedule = concertRepository.getConcertSchedule(concertSeat.getConcertScheduleId());
        Concert concert = concertRepository.getConcert(concertSchedule.getConcertId());
        User user = userRepository.findUserWithLockById(concertBooking.getUserId());

        user.usePoints(concertSeat.getPrice());
        concertBooking.updateBookingStatusToCompleted();

        Payment payment = paymentRepository.savePayment(concertBooking.getConcertBookingId(), concertSeat.getPrice());

        PaymentEventInfo paymentEventInfo = PaymentEventInfo.builder()
                        .userName(user.getUserName())
                        .concertName(concert.getConcertName())
                        .concertDate(concertSchedule.getConcertDate())
                        .seatNumber(concertSeat.getSeatNumber())
                        .price(concertSeat.getPrice())
                        .concertBookingId(concertBooking.getConcertBookingId())
                        .paymentId(payment.getPaymentId())
                        .build();

        String paymentCompletedToken = input.authorizationHeader().substring(7);
        OutBox outBox = outBoxJpaRepository.save(OutBox.of("payment", paymentCompletedToken , "payment-success", paymentEventInfo));

        paymentEventPublisher.success(new PaymentSuccessEvent(outBox));

        return new PaymentInfo.Output(payment.getPaymentId());
    }
}
