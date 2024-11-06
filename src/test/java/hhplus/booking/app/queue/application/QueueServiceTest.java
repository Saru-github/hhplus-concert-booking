//package hhplus.booking.app.queue.application;
//
//import hhplus.booking.app.queue.application.dto.QueueInfo;
//import hhplus.booking.app.queue.domain.entity.Queue;
//import hhplus.booking.app.queue.domain.repository.QueueRepository;
//import hhplus.booking.app.queue.infra.jpa.QueueJpaRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.cache.CacheManager;
//import org.springframework.cache.annotation.EnableCaching;
//
//import java.time.LocalDateTime;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.ArgumentMatchers.anyString;
//import static org.mockito.Mockito.*;
//
//@SpringBootTest
//public class QueueServiceTest {
//
//    @Autowired
//    private QueueRepository queueRepository;
//
//    @Autowired
//    private QueueService queueService;
//
//    private QueueJpaRepository queueJpaRepository;
//    private static final String TEST_TOKEN = "TEST_UUID_TOKEN";
//
//    @Test
//    public void testGetQueueInfo_CacheMiss() {
//        QueueInfo.Input input = new QueueInfo.Input("Bearer " + TEST_TOKEN);
//        Queue queue = new Queue(TEST_TOKEN, "WAITING", LocalDateTime.now().plusMinutes(1));
//
//        queueJpaRepository.save(queue);
//
//        QueueInfo.Output output = queueService.getQueueInfo(input);
//
//        assertEquals(queue.getTokenValue(), output.tokenValue());
//        assertEquals("WAITING", output.status());
//
//        // Repository가 실제로 호출되었는지 확인
//        verify(queueRepository, times(1)).getQueue(TEST_TOKEN);
//    }
//
//    @Test
//    public void testGetQueueInfo_CacheHit() {
//        QueueInfo.Input input = new QueueInfo.Input("Bearer " + TEST_TOKEN);
//        Queue queue = new Queue(TEST_TOKEN, "WAITING", LocalDateTime.now().plusMinutes(1));
//
//        // Cache에 저장 후 CacheHit 테스트
//        when(queueRepository.getQueue(anyString())).thenReturn(queue);
//        when(queueRepository.findWaitingQueues()).thenReturn(List.of(queue));
//
//        // 캐시 첫 조회
//        QueueInfo.Output firstOutput = queueService.getQueueInfo(input);
//
//        // 캐시된 데이터를 가져오는지 확인
//        QueueInfo.Output cachedOutput = queueService.getQueueInfo(input);
//
//        assertEquals(firstOutput, cachedOutput);
//        verify(queueRepository, times(1)).getQueue(TEST_TOKEN); // Repository 호출은 최초 1회만 발생
//    }
//
//    @Test
//    public void testUpdateQueueStatus_CacheEvict() {
//        Queue queue = new Queue(TEST_TOKEN, "WAITING", LocalDateTime.now().plusMinutes(1));
//
//        // 캐시에서 제거됨을 확인하기 위한 CacheEvict 테스트
//        queueService.updateQueueStatus(queue, "PROCESSING");
//
//        // 캐시 갱신 여부 확인
//        assertEquals("PROCESSING", queue.getStatus());
//        verify(queueJpaRepository, times(1)).save(queue); // 데이터 저장 확인
//    }
//}
