package hhplus.booking.app.user.interfaces.api;

import hhplus.booking.app.user.application.dto.UserPointInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@Tag(name = "USER", description = "유저 및 포인트 관련 API")
public interface UserControllerDocs {

    @Operation(summary = "유저 포인트 조회", description = "입력받은 유저 정보를 조회하고, 포인트 잔액을 반환합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "유저 정보 및 포인트 정상 반환")
    })
    public ResponseEntity<UserPointInfo.Output> getUserPoints(
            @RequestHeader(value = "Authorization", required = true) String authorizationHeader,
            @PathVariable("userId") Long userId
    );

    @Operation(summary = "유저 포인트 충전", description = "입력받은 유저 정보로 요청한 양의 포인트를 충전합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "유저 정보 및 충전된 포인트 정상 반환"),
    })
    public ResponseEntity<UserPointInfo.Output> chargeUserPoints(
            @RequestHeader(value = "Authorization", required = true) String authorizationHeader,
            @PathVariable("userId") Long userId,
            @RequestBody Long amount
    );

    @Operation(summary = "유저 포인트 사용", description = "입력받은 유저 정보로 요청한 양의 포인트를 사용합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "유저 정보 및 사용된 포인트 정상 반환"),
    })
    public ResponseEntity<UserPointInfo.Output> useUserPoints(
            @RequestHeader(value = "Authorization", required = true) String authorizationHeader,
            @PathVariable("userId") Long userId,
            @RequestBody Long amount
    );
}