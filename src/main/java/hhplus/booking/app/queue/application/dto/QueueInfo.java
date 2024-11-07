package hhplus.booking.app.queue.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
public class QueueInfo {

    public record Input(
            String authorizationHeader
    ) {}

    @Builder
    @Schema(name = "QueueOutput")
    public record Output(
            String tokenValue,
            Long rank,
            String status
    ) {}
}
