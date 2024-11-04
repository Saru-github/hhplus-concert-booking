package hhplus.booking.config.database;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import redis.embedded.RedisServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.BindException;

@Slf4j
@Configuration
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
}
