package monster.jhentai.interceptor;

import com.alibaba.fastjson.JSON;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import monster.jhentai.annotation.LoginRequired;
import monster.jhentai.model.bo.JHenTaiUser;
import monster.jhentai.model.response.Result;
import monster.jhentai.threadlocal.JHenTaiUserThreadLocal;
import monster.jhentai.util.IpUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;


/**
 * @author JTMonster
 * @date 2024/5/25
 */
@Component
@Slf4j
public class LoginRequiredInterceptor implements HandlerInterceptor {


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        try {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            if (handlerMethod.hasMethodAnnotation(LoginRequired.class) || handlerMethod.getBeanType().isAnnotationPresent(LoginRequired.class)) {
                JHenTaiUser jHenTaiUser = JHenTaiUserThreadLocal.get();
                if (jHenTaiUser == null) {
                    log.warn("LoginRequiredInterceptor preHandle failed, user not login, uri:{}, ip:{}", request.getRequestURI(), IpUtil.getClientIpAddress(request));
                    response.setStatus(HttpStatus.UNAUTHORIZED.value());
                    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                    JSON.writeJSONString(response.getOutputStream(), Result.error(HttpStatus.UNAUTHORIZED.getReasonPhrase()));
                    response.getOutputStream().flush();
                    return false;
                }
            }
        } catch (Exception e) {
            log.error("LoginRequiredInterceptor error", e);
            return false;
        }

        return true;
    }
}
