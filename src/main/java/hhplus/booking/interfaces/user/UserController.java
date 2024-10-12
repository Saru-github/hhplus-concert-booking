package hhplus.booking.interfaces.user;

import hhplus.booking.interfaces.user.dto.UserQueueInfo;
import hhplus.booking.interfaces.user.dto.UserTokenInfo;
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


    @PostMapping("/{userId}/token")
    public ResponseEntity<UserTokenInfo.Output> getUserTokenInfo (
            @PathVariable ("userId") Long userId) {

        return ResponseEntity.ok(new UserTokenInfo.Output("abcd1234efgh5678"));
    }

    @GetMapping("/{userId}/queue")
    public ResponseEntity<UserQueueInfo.Output> getUserQueueRank(
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable("userId") Long userId) {

        return ResponseEntity.ok(new UserQueueInfo.Output(1L, 1200L));
    }
}
