package hhplus.booking.config.database.redis;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.util.StringUtils;
import redis.embedded.RedisServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.BindException;
import java.time.Duration;

@Slf4j
@Configuration
@EnableCaching
public class EmbeddedRedisConfig {

    @Value("${spring.redis.port:6379}") // 기본 포트 설정
    private int redisPort;

    private RedisServer redisServer;

    @PostConstruct
    public void startRedis() {
        try {
            int port = isRedisRunning(redisPort) ? findAvailablePort() : redisPort;
            redisServer = new RedisServer(port);
            redisServer.start();
            log.info("Embedded Redis started on port: {}", port);
        } catch (IOException e) {
            log.error("Failed to start embedded Redis", e);
            throw new IllegalStateException("Could not start embedded Redis", e);
        }
    }

    @PreDestroy
    public void stopRedis() throws IOException {
        if (redisServer != null && redisServer.isActive()) {
            redisServer.stop();
            log.info("Embedded Redis stopped");
        }
    }

    /**
     * 현재 Redis 서버가 실행 중인지 확인합니다.
     */
    private boolean isRedisRunning(int port) throws IOException {
        return isProcessRunning(executePortCheckCommand(port));
    }

    /**
     * Redis가 실행 중이 아닐 경우, 사용할 수 있는 포트를 찾습니다.
     */
    private int findAvailablePort() throws IOException {
        for (int port = 10000; port <= 65535; port++) {
            if (!isProcessRunning(executePortCheckCommand(port))) {
                log.info("Available port found for Redis: {}", port);
                return port;
            }
        }
        throw new BindException("No available ports found for embedded Redis.");
    }

    /**
     * netstat 명령어를 통해 해당 포트를 사용 중인 프로세스가 있는지 확인하는 명령을 실행합니다.
     */
    private Process executePortCheckCommand(int port) throws IOException {
        // Windows 명령어
        String command = String.format("netstat -ano | findstr LISTENING | findstr %d", port);
        return Runtime.getRuntime().exec(new String[]{"cmd.exe", "/c", command});
    }

    /**
     * 실행 중인 프로세스를 확인하여 해당 포트를 사용하는지 여부를 반환합니다.
     */
    private boolean isProcessRunning(Process process) {
        try (BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            return input.lines().anyMatch(StringUtils::hasText);
        } catch (IOException e) {
            log.error("Error reading process input stream", e);
            throw new IllegalStateException("Failed to verify if Redis process is running", e);
        }
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);

        // ObjectMapper 설정
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.activateDefaultTyping(
                LaissezFaireSubTypeValidator.instance,
                ObjectMapper.DefaultTyping.NON_FINAL,
                JsonTypeInfo.As.PROPERTY
        );

        objectMapper.registerModule(new JavaTimeModule()); // Java 8 날짜/시간 타입 지원

        // Jackson2JsonRedisSerializer 설정
        Jackson2JsonRedisSerializer<Object> jsonRedisSerializer = new Jackson2JsonRedisSerializer<>(objectMapper, Object.class);

        // RedisTemplate 직렬화기 설정
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(jsonRedisSerializer);
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(jsonRedisSerializer);

        return redisTemplate;
    }

    @Bean
    public ZSetOperations<String, String> zSetOperations(RedisTemplate<String, String> redisTemplate) {
        return redisTemplate.opsForZSet();
    }

    @Bean
    public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofHours(3)) // 캐시 유효 기간 설정 (예: 3시간)
                .disableCachingNullValues();   // null 값 캐싱 방지

        return RedisCacheManager.builder(redisConnectionFactory)
                .cacheDefaults(redisCacheConfiguration)
                .build();
    }
}

