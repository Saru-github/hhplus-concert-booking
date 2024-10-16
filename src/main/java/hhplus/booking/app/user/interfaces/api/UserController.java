package hhplus.booking.app.user.interfaces.api;

import hhplus.booking.app.queue.application.QueueService;
import hhplus.booking.app.queue.application.dto.QueueInfo;
import hhplus.booking.app.user.application.UserService;
import hhplus.booking.app.user.application.dto.UserPointInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/booking/user")
public class UserController {

    private final UserService userService;

    @GetMapping("/{userId}/points")
    public ResponseEntity<UserPointInfo.Output> getUserPoints(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader,
            @PathVariable("userId") Long userId
    ) {
        return ResponseEntity.ok(userService.getUserPoints(new UserPointInfo.Input(userId, null)));
    }

    @PutMapping("/{userId}/points/charge")
    public ResponseEntity<UserPointInfo.Output> chargeUserPoints(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader,
            @PathVariable("userId") Long userId,
            @RequestBody Long amount
    ) {
        return ResponseEntity.ok(userService.chargeUserPoints(new UserPointInfo.Input(userId, amount)));
    }

    @PutMapping("/{userId}/points/use")
    public ResponseEntity<UserPointInfo.Output> useUserPoints(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader,
            @PathVariable("userId") Long userId,
            @RequestBody Long amount
    ) {
        return ResponseEntity.ok(userService.useUserPoints(new UserPointInfo.Input(userId, amount)));
    }
}
