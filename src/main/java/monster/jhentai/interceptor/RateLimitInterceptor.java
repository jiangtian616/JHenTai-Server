package monster.jhentai.interceptor;

import com.alibaba.fastjson.JSON;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.util.concurrent.RateLimiter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import monster.jhentai.model.response.Result;
import monster.jhentai.util.IpUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * @author JTMonster
 * @date 2024/5/25
 */
@Component
@Slf4j
public class RateLimitInterceptor implements HandlerInterceptor {
    private final Cache<String, RateLimiter> limiters = CacheBuilder.newBuilder()
            .expireAfterAccess(Duration.ofMinutes(1))
            .build();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String clientIp = IpUtil.getClientIpAddress(request);
        RateLimiter rateLimiter = limiters.get(clientIp, () -> RateLimiter.create(5));

        if (!rateLimiter.tryAcquire()) {
            log.warn("RateLimitInterceptor preHandle failed, rate limit, uri:{}, ip:{}", request.getRequestURI(), clientIp);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            JSON.writeJSONString(response.getOutputStream(), Result.error(HttpStatus.TOO_MANY_REQUESTS.value(), HttpStatus.TOO_MANY_REQUESTS.getReasonPhrase()));
            response.getOutputStream().flush();
            return false;
        }

        return true;
    }

    @Scheduled(fixedRate = 1, timeUnit = TimeUnit.HOURS, initialDelay = 1)
    public void cleanup() {
        log.info("Cleaning up rate limiters, before size:{}", limiters.size());
        limiters.cleanUp();
        log.info("Cleaning up rate limiters, after size:{}", limiters.size());
    }
}
