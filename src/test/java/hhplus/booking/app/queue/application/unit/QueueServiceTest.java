//package hhplus.booking.app.queue.application.unit;
//
//import hhplus.booking.app.queue.application.QueueService;
//import hhplus.booking.app.queue.application.dto.QueueInfo;
//import hhplus.booking.app.queue.domain.entity.Queue;
//import hhplus.booking.app.queue.domain.repository.QueueRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.UUID;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.mockito.BDDMockito.anyString;
//import static org.mockito.BDDMockito.given;
//
//@ExtendWith(MockitoExtension.class)
//class QueueServiceTest {
//
//    @Mock
//    private QueueRepository queueRepository;
//
//    @InjectMocks
//    private QueueService queueService;
//
//    Queue queue;
//    String tokenValue;
//
//    @BeforeEach
//    void setUp() {
//        tokenValue = UUID.randomUUID().toString();
//        queue = new Queue(1L, tokenValue, "WAITING", LocalDateTime.now().plusMinutes(30), LocalDateTime.now(), LocalDateTime.now());
//    }
//
//    @Test
//    @DisplayName("[성공] 토큰 없을때, 토큰 발급 후 대기열 순번 리턴")
//    void successTestGetQueueInfoWithNoTokenValue() {
//
//        // given
//        given(queueRepository.registerQueue()).willReturn(queue);
//        given(queueRepository.getQueue(anyString())).willReturn(queue);
//        given(queueRepository.findWaitingQueues()).willReturn(List.of(queue));
//
//        // when
//        QueueInfo.Output result = queueService.getQueueInfo(new QueueInfo.Input(null));
//
//        // then
//        assertThat(result.tokenValue()).isEqualTo(tokenValue);
//        assertThat(result.rank()).isEqualTo(1L);
//
//    }
//
//    @Test
//    @DisplayName("[성공] 잘못된 토큰 정보 입력했을 때, 토큰 재발급 후 대기열 순번 리턴")
//    void successTestGetQueueInfoWithWrongTokenValue() {
//
//        String wrongToken = "wrongTokenValue";
//
//        // given
//        given(queueRepository.getQueue(anyString())).willReturn(queue);
//        given(queueRepository.findWaitingQueues()).willReturn(List.of(queue));
//
//        // when
//        QueueInfo.Output result = queueService.getQueueInfo(new QueueInfo.Input(wrongToken));
//
//        // then
//        assertThat(result.tokenValue()).isEqualTo(tokenValue);
//        assertThat(result.rank()).isEqualTo(1L);
//
//    }
//
//    @Test
//    @DisplayName("[성공] 올바른 토큰 정보 입력했을 때, 토큰 검증 후 대기열 5번 리턴")
//    void successTestGetQueueInfoWithTokenValue() {
//
//        // given
//        Queue queue2 = new Queue(2L, tokenValue, "WAITING", LocalDateTime.now().plusMinutes(30), LocalDateTime.now(), LocalDateTime.now());
//        Queue queue3 = new Queue(3L, tokenValue, "WAITING", LocalDateTime.now().plusMinutes(30), LocalDateTime.now(), LocalDateTime.now());
//        Queue queue4 = new Queue(4L, tokenValue, "WAITING", LocalDateTime.now().plusMinutes(30), LocalDateTime.now(), LocalDateTime.now());
//        Queue queue5 = new Queue(5L, tokenValue, "WAITING", LocalDateTime.now().plusMinutes(30), LocalDateTime.now(), LocalDateTime.now());
//
//        given(queueRepository.getQueue(anyString())).willReturn(queue5);
//        given(queueRepository.findWaitingQueues()).willReturn(List.of(queue, queue2, queue3, queue4, queue5));
//
//        // when
//        QueueInfo.Output result = queueService.getQueueInfo(new QueueInfo.Input(tokenValue));
//
//        // then
//        assertThat(result.tokenValue()).isEqualTo(tokenValue);
//        assertThat(result.rank()).isEqualTo(5L);
//
//    }
//
//}