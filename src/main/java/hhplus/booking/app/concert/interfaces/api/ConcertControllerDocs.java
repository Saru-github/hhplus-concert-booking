package hhplus.booking.app.concert.interfaces.api;

import hhplus.booking.app.concert.application.dto.ConcertBookingInfo;
import hhplus.booking.app.concert.application.dto.ConcertScheduleInfo;
import hhplus.booking.app.concert.application.dto.ConcertSeatInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Tag(name = "CONCERT", description = "콘서트 및 예약 관련 API")
public interface ConcertControllerDocs {

    @Operation(summary = "콘서트 일정 조회", description = "입력받은 콘서트 일정을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "콘서트의 일정이 정상 조회됩니다.")
    })
    public ResponseEntity<List<ConcertScheduleInfo.Output>> getConcertDates(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestParam("concertId") Long concertId
    );

    @Operation(summary = "콘서트 예약 가능 좌석 조회", description = "입력받은 콘서트 일정의 예약가능한 좌석들을 조회합니다..")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "예약가능한 콘서트 좌석들이 정상 조회됩니다."),
    })
    public ResponseEntity<List<ConcertSeatInfo.Output>> getAvailableConcertSeats(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestParam("concertScheduleId") Long concertScheduleId
    );

    @Operation(summary = "콘서트 예약", description = "입력받은 예약가능한 콘서트 좌석을 예약합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "콘서트 예약이 정상 진행 되며 예약 정보가 조회됩니다."),
    })
    public ResponseEntity<ConcertBookingInfo.Output> bookConcertSeat(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestBody ConcertBookingInfo.Input input
    );
}