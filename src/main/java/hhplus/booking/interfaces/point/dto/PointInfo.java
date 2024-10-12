package hhplus.booking.interfaces.point.dto;

import lombok.Builder;

public class PointInfo {
    public record Input(Long userId, Long amount) {}
    @Builder
    public record Output(Long pointId, Long userId, Long balance) {}
}
