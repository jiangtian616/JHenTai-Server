package monster.jhentai.interceptor;

import com.alibaba.fastjson.JSON;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import monster.jhentai.annotation.ApiKeyRequired;
import monster.jhentai.enums.ErrorCodeEnum;
import monster.jhentai.model.response.Result;
import monster.jhentai.service.ApiKeyService;
import monster.jhentai.service.NonceService;
import monster.jhentai.util.IpUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@Slf4j
public class ApiKeyInterceptor implements HandlerInterceptor {

    private static final String APP_ID_HEADER = "X-App-Id";
    private static final String TIMESTAMP_HEADER = "X-Timestamp";
    private static final String NONCE_HEADER = "X-Nonce";
    private static final String SIGNATURE_HEADER = "X-Signature";

    @Autowired
    private ApiKeyService apiKeyService;

    @Autowired
    private NonceService nonceService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        try {
            if (handler instanceof HandlerMethod) {
                HandlerMethod handlerMethod = (HandlerMethod) handler;
                if (handlerMethod.hasMethodAnnotation(ApiKeyRequired.class) || handlerMethod.getBeanType().isAnnotationPresent(ApiKeyRequired.class)) {
                    String appId = request.getHeader(APP_ID_HEADER);
                    String timestampStr = request.getHeader(TIMESTAMP_HEADER);
                    String nonce = request.getHeader(NONCE_HEADER);
                    String signature = request.getHeader(SIGNATURE_HEADER);

                    if (StringUtils.isAnyBlank(appId, timestampStr, nonce, signature)) {
                        log.warn("ApiKeyInterceptor preHandle failed, missing headers, uri:{}, ip:{}", request.getRequestURI(), IpUtil.getClientIpAddress(request));
                        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                        JSON.writeJSONString(response.getOutputStream(), Result.error(ErrorCodeEnum.UNAUTHORIZED.getCode(), "Missing required headers"));
                        response.getOutputStream().flush();
                        return false;
                    }

                    long timestamp;
                    try {
                        timestamp = Long.parseLong(timestampStr);
                    } catch (NumberFormatException e) {
                        log.warn("Invalid timestamp format: {}", timestampStr, e);
                        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                        JSON.writeJSONString(response.getOutputStream(), Result.error(ErrorCodeEnum.UNAUTHORIZED.getCode(), "Invalid timestamp format"));
                        response.getOutputStream().flush();
                        return false;
                    }

                    if (!nonceService.isValidNonce(appId, nonce, timestamp)) {
                        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                        JSON.writeJSONString(response.getOutputStream(), Result.error(ErrorCodeEnum.UNAUTHORIZED.getCode(), "Duplicate nonce"));
                        response.getOutputStream().flush();
                        return false;
                    }

                    if (!apiKeyService.validateSignature(appId, timestamp, nonce, signature)) {
                        log.warn("ApiKeyInterceptor preHandle failed, invalid API key, uri:{}, ip:{}", request.getRequestURI(), IpUtil.getClientIpAddress(request));
                        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                        JSON.writeJSONString(response.getOutputStream(), Result.error(ErrorCodeEnum.UNAUTHORIZED.getCode(), "Invalid or expired signature"));
                        response.getOutputStream().flush();
                        return false;
                    }
                }
            }
        } catch (Exception e) {
            log.error("ApiKeyInterceptor error", e);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            JSON.writeJSONString(response.getOutputStream(), Result.error(ErrorCodeEnum.UNAUTHORIZED.getCode(), "API key validation failed"));
            response.getOutputStream().flush();
            return false;
        }

        return true;
    }
} 