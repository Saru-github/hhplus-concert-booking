package hhplus.booking.app.payment.interfaces.api.dto;

import lombok.Builder;

public class PaymentInfo {
    public record Input(Long userId, Long amount) {}
    @Builder
    public record Output(Long pointId, Long userId, Long balance) {}
}
