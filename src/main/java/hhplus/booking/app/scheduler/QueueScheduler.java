package hhplus.booking.app.scheduler;

import hhplus.booking.app.queue.domain.entity.Queue;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class QueueScheduler {
    // TODO: 대기열 WAITING -> PROCESSING, 만료 등 스케쥴러

    public List<Queue> enterProcessingScheduler() {

        // TODO: QUEUE 의 MAX 사이즈 - PROCESSING 수 = 업데이트 해줄 수
        // TODO: WAITING 인 것들 중에 등록순으로 잘라서, 업데이트 해줄 수 만큼만 뽑음
        // TODO: 그만큼만 WAITING -> PROCESSING 업데이트

        return null;
    }
}
