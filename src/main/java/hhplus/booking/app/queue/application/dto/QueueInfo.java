package hhplus.booking.app.queue.application.dto;

import lombok.Builder;

public class QueueInfo {
    public record Input(String authorizationHeader) {}
    @Builder
    public record Output(Long queueId, String tokenValue, Long rank, String status) {}
}
