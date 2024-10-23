package hhplus.booking.config.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;

@Slf4j
@Component
@WebFilter(urlPatterns = "/booking/*")
public class GlobalFilter implements Filter {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ObjectWriter writer = objectMapper.writerWithDefaultPrettyPrinter();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper((HttpServletRequest) servletRequest);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper((HttpServletResponse) servletResponse);

        filterChain.doFilter(requestWrapper, responseWrapper);

        String url = requestWrapper.getRequestURI();
        String authorizationHeader = requestWrapper.getHeader("Authorization");
        String requestContent = parseJsonOrDefault(new String(requestWrapper.getContentAsByteArray()));

        log.info("request url: {}, request token: {}, \nrequest body: {}", url, authorizationHeader, requestContent);

        String responseContent = parseJsonOrDefault(new String(responseWrapper.getContentAsByteArray()));
        int httpStatus = responseWrapper.getStatus();

        responseWrapper.copyBodyToResponse();

        log.info("response Status : {},\n response body: {}\n", httpStatus, responseContent);

    }

    private String parseJsonOrDefault(String content) {
        try {
            Object json = objectMapper.readValue(content, Object.class);
            return writer.writeValueAsString(json);
        } catch (Exception e) {
            return content; // JSON 파싱 실패 시 원본 그대로 반환
        }
    }
}
