package hhplus.booking.app;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class EmbeddedRedisTest {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Test
    public void testRedisConnection() {
        // Redis에 데이터 저장
        redisTemplate.opsForValue().set("testKey", "testValue");

        // Redis에서 데이터 조회
        String value = redisTemplate.opsForValue().get("testKey");

        // 데이터 검증
        assertThat(value).isEqualTo("testValue");
    }
}