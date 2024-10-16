package hhplus.booking.app.queue.application.dto;

import hhplus.booking.app.queue.domain.entity.Queue;
import lombok.Builder;

public class QueueInfo {
    public record Input(String authorizationHeader) {}
    @Builder
    public record Output(Long queueId, String tokenValue, Long rank, String status) {
        public Output(Queue queue, Long rank) {
            this(
                    queue.getQueueId(),
                    queue.getTokenValue(),
                    rank,
                    queue.getStatus()
            );
        }
    }
}
