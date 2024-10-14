package hhplus.booking.app.point.interfaces.api;

import hhplus.booking.app.point.interfaces.api.dto.PointInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/points")
public class PointController {

    @GetMapping("/{userId}")
    public ResponseEntity<PointInfo.Output> getUserPoint(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestBody PointInfo.Input input) {

        return ResponseEntity.ok(PointInfo.Output.builder()
                .pointId(1L)
                .userId(1L)
                .balance(10000L)
                .build());
    }

    @PutMapping("/{userId}/charge")
    public ResponseEntity<PointInfo.Output> chargePoints(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestBody PointInfo.Input input) {

        return ResponseEntity.ok(PointInfo.Output.builder()
                        .pointId(1L)
                        .userId(1L)
                        .balance(10000L)
                        .build());
    }
}
