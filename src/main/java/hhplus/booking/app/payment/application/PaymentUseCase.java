package hhplus.booking.app.payment.application;

import hhplus.booking.app.concert.domain.entity.ConcertBooking;
import hhplus.booking.app.concert.domain.entity.ConcertSeat;
import hhplus.booking.app.concert.domain.repository.ConcertRepository;
import hhplus.booking.app.payment.application.dto.PaymentInfo;
import hhplus.booking.app.payment.domain.PaymentEventPublisher;
import hhplus.booking.app.payment.domain.PaymentSuccessEvent;
import hhplus.booking.app.payment.domain.entity.Payment;
import hhplus.booking.app.payment.domain.repository.PaymentRepository;
import hhplus.booking.app.queue.domain.repository.QueueRepository;
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
    private final QueueRepository queueRepository;
    private final PaymentEventPublisher paymentEventPublisher;

    @Transactional
    public PaymentInfo.Output processPayment(PaymentInfo.Input input) {

        ConcertBooking concertBooking = concertRepository.getConcertBooking(input.concertBookingId());
        concertBooking.validConcertBookingStatus();

        ConcertSeat concertSeat = concertRepository.getConcertSeat(concertBooking.getConcertSeatId());
        User user = userRepository.findUserWithLockById(concertBooking.getUserId());

        user.usePoints(concertSeat.getPrice());

        Payment payment = paymentRepository.savePayment(concertBooking.getConcertBookingId(), concertSeat.getPrice());
        concertBooking.updateBookingStatusToCompleted();
        queueRepository.deleteProcessingToken(input.authorizationHeader().substring(7));
        paymentEventPublisher.success(new PaymentSuccessEvent(payment.getPaymentId(), payment.getConcertBookingId()));

        return new PaymentInfo.Output(payment.getPaymentId());
    }
}
