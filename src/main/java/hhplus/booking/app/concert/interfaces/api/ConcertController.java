package hhplus.booking.app.concert.interfaces.api;

import hhplus.booking.app.concert.application.ConcertService;
import hhplus.booking.app.concert.application.dto.ConcertBookingInfo;
import hhplus.booking.app.concert.application.dto.ConcertScheduleInfo;
import hhplus.booking.app.concert.application.dto.ConcertSeatInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/booking/concerts")
public class ConcertController implements ConcertControllerDocs{

    private final ConcertService concertService;

    @GetMapping("/dates")
    public ResponseEntity<List<ConcertScheduleInfo.Output>> getConcertDates(
            @RequestParam("concertId") Long concertId
    ) {

        return ResponseEntity.ok(concertService.getConcertScheduleInfos(new ConcertScheduleInfo.Input(concertId)));
    }

    @GetMapping("/seats")
    public ResponseEntity<List<ConcertSeatInfo.Output>> getAvailableConcertSeats(
            @RequestParam("concertScheduleId") Long concertScheduleId
    ) {

        return ResponseEntity.ok(concertService.getAvailableConcertSeatInfos(new ConcertSeatInfo.Input(concertScheduleId)));
    }

    @PostMapping("/booking")
    public ResponseEntity<ConcertBookingInfo.Output> bookConcertSeat(
            @RequestBody ConcertBookingInfo.Input input
    ) {
        return ResponseEntity.ok(concertService.bookConcertSeat(input));
    }
}
