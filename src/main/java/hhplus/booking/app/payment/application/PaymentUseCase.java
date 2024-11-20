package hhplus.booking.app.payment.application;

import hhplus.booking.app.concert.application.ConcertService;
import hhplus.booking.app.concert.domain.entity.Concert;
import hhplus.booking.app.concert.domain.entity.ConcertBooking;
import hhplus.booking.app.concert.domain.entity.ConcertSchedule;
import hhplus.booking.app.concert.domain.entity.ConcertSeat;
import hhplus.booking.app.concert.domain.repository.ConcertRepository;
import hhplus.booking.app.concert.infra.jpa.ConcertJpaRepository;
import hhplus.booking.app.concert.infra.jpa.ConcertScheduleJpaRepository;
import hhplus.booking.app.payment.application.dto.PaymentInfo;
import hhplus.booking.app.payment.domain.entity.Payment;
import hhplus.booking.app.payment.domain.event.kafka.PaymentEventPublisher;
import hhplus.booking.app.payment.domain.event.kafka.dto.KafkaPaymentSuccessEvent;
import hhplus.booking.app.payment.domain.repository.PaymentRepository;
import hhplus.booking.app.queue.domain.repository.QueueRepository;
import hhplus.booking.app.user.domain.entity.User;
import hhplus.booking.app.user.domain.repository.UserRepository;
import hhplus.booking.config.exception.BusinessException;
import hhplus.booking.config.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class PaymentUseCase {

    private final UserRepository userRepository;
    private final ConcertRepository concertRepository;
    private final QueueRepository queueRepository;
    private final PaymentRepository paymentRepository;
    private final PaymentEventPublisher paymentEventPublisher;
    private final ConcertJpaRepository concertJpaRepository;
    private final ConcertScheduleJpaRepository concertScheduleJpaRepository;
    private final ConcertService concertService;

    @Transactional
    public PaymentInfo.Output processPayment(PaymentInfo.Input input) {

        ConcertBooking concertBooking = concertRepository.getConcertBooking(input.concertBookingId());
        concertBooking.validConcertBookingStatus();

        ConcertSeat concertSeat = concertRepository.getConcertSeat(concertBooking.getConcertSeatId());
        ConcertSchedule concertSchedule = concertScheduleJpaRepository.findById(concertSeat.getConcertScheduleId())
                .orElseThrow(() -> new BusinessException(ErrorCode.SCHEDULE_NOT_FOUND));

        Concert concert = concertJpaRepository.findById(concertSchedule.getConcertId())
                .orElseThrow(() -> new BusinessException(ErrorCode.CONCERT_NOT_FOUND));

        User user = userRepository.findUserWithLockById(concertBooking.getUserId());

        user.usePoints(concertSeat.getPrice());
        concertBooking.updateBookingStatusToCompleted();

        Payment payment = paymentRepository.savePayment(concertBooking.getConcertBookingId(), concertSeat.getPrice());

        String paymentCompletedToken = input.authorizationHeader().substring(7);
        queueRepository.deleteProcessingToken(paymentCompletedToken);

        KafkaPaymentSuccessEvent paymentEventInfo = KafkaPaymentSuccessEvent.builder()
                .userName(user.getUserName())
                .concertName(concert.getConcertName())
                .concertDate(concertSchedule.getConcertDate().toString())
                .seatNumber(concertSeat.getSeatNumber())
                .price(concertSeat.getPrice())
                .concertBookingId(concertBooking.getConcertBookingId())
                .paymentId(payment.getPaymentId())
                .build();

        paymentEventPublisher.success(paymentEventInfo);

        return new PaymentInfo.Output(payment.getPaymentId());
    }
}
