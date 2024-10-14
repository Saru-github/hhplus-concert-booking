package hhplus.booking.app.booking.interfaces.api;

import hhplus.booking.app.booking.interfaces.api.dto.CreateBookingInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/booking")
public class BookingController {

    @PostMapping("/booking/temporary")
    public ResponseEntity<CreateBookingInfo.Output> createBooking(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestBody CreateBookingInfo.Input input) {

        return ResponseEntity.ok(CreateBookingInfo.Output.builder()
                .bookingId(1L)
                .userId(1L)
                .concertId(1L)
                .concertScheduleId(1L)
                .concertSeatId(1L)
                .expiredAt(LocalDateTime.now().plusHours(1))
                .status("PROCESSING")
                .build())
                ;
    }

    @PutMapping("/booking/payments")
    public ResponseEntity<CreateBookingInfo.Output> completeReservation(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestBody CreateBookingInfo.Input input) {

        return ResponseEntity.ok(CreateBookingInfo.Output.builder()
                .bookingId(1L)
                .userId(1L)
                .concertId(1L)
                .concertScheduleId(1L)
                .concertSeatId(1L)
                .amountUsed(2000L)
                .status("COMPLETE")
                .build())
                ;
    }
}
