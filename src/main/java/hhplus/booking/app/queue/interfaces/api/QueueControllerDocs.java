package hhplus.booking.app.queue.interfaces.api;

import hhplus.booking.app.queue.application.dto.QueueInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;

@Tag(name = "QUEUE", description = "대기열 관련 API")
public interface QueueControllerDocs {

    @Operation(summary = "대기열 토큰 및 대기열순번 반환", description = "토큰이 없는 경우 발급 후 토큰과 대기열 순번을 반환 하고, 있는 경우 검증 후 대기열 순번을 반환합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "토큰 및 대기열 순번, 대기열 상태 정상 반환"),
    })
    public ResponseEntity<QueueInfo.Output> getUserQueueRank(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader
    );
}