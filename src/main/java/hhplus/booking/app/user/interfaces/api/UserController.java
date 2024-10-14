package hhplus.booking.app.user.interfaces.api;

import hhplus.booking.app.user.application.UserService;
import hhplus.booking.app.user.interfaces.api.dto.UserQueueInfo;
import hhplus.booking.app.user.interfaces.api.dto.UserTokenInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/booking/user")
public class UserController {

    private final UserService userService;

    @GetMapping("/{userId}/queue")
    public ResponseEntity<UserQueueInfo.Output> getUserQueueRank(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader,
            @PathVariable("userId") Long userId) {

        UserQueueInfo.Output userQueueInfo = userService.getUserQueueRank(new UserQueueInfo.Input(new UserTokenInfo(authorizationHeader, userId)));

        return ResponseEntity.ok()
                .header(userQueueInfo.userTokenInfo().tokenValue())
                .body(userQueueInfo);
    }
}
