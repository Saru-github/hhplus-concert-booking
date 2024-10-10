package hhplus.booking.interfaces.concert;

import hhplus.booking.interfaces.concert.dto.ConcertDateInfo;
import hhplus.booking.interfaces.concert.dto.ConcertSeatInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/booking/concerts")
public class ConcertController {

    @GetMapping("/{concertId}/dates")
    public ResponseEntity<List<ConcertDateInfo.Output>> getAvailableConcertDates(
            @PathVariable("concertId") Long concertId,
            @RequestHeader("Authorization") String authorizationHeader) {

        List<ConcertDateInfo.Output> schedules = Arrays.asList(
                new ConcertDateInfo.Output(1L, "2024-10-10"),
                new ConcertDateInfo.Output(2L, "2024-10-11"),
                new ConcertDateInfo.Output(3L, "2024-10-12")
        );

        return ResponseEntity.ok(schedules);
    }

    @GetMapping("/{concertId}/seats")
    public ResponseEntity<List<ConcertSeatInfo.Output>> getAvailableConcertSeats(
            @PathVariable("concertId") Long concertId,
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestBody ConcertSeatInfo.Input input) {

        // 예시 좌석 데이터
        List<ConcertSeatInfo.Output> seats = Arrays.asList(
                new ConcertSeatInfo.Output(1L, 1L),
                new ConcertSeatInfo.Output(3L, 3L),
                new ConcertSeatInfo.Output(11L, 11L)
        );

        return ResponseEntity.ok(seats);
    }
}
