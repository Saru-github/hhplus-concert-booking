package hhplus.booking.app;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;


// ExtendWith 로 사용
@Slf4j
public class ExecutionTimeWatcher implements TestWatcher, BeforeTestExecutionCallback {

    private long startTime;

    @Override
    public void beforeTestExecution(ExtensionContext extensionContext) {
        startTime = System.currentTimeMillis();
    }

    @Override
    public void testSuccessful(ExtensionContext context) {
        printExecutionTime(context, "성공");
    }

    @Override
    public void testFailed(ExtensionContext context, Throwable cause) {
        printExecutionTime(context, "실패");
    }

    private void printExecutionTime(ExtensionContext context, String result) {
        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;
        log.info("\n테스트 {} - {}: 실행 시간 = {} ms", result, context.getDisplayName(), executionTime);
    }
}
