package hhplus.booking.app.queue.interfaces.api;

import hhplus.booking.app.queue.application.QueueService;
import hhplus.booking.app.queue.application.dto.QueueInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/booking/queue")
public class QueueController {

    private final QueueService queueService;

    @GetMapping("/status")
    public ResponseEntity<QueueInfo.Output> getUserQueueRank(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader
    ) {
        return ResponseEntity.ok(queueService.getQueueInfo(new QueueInfo.Input(authorizationHeader)));
    }
}
