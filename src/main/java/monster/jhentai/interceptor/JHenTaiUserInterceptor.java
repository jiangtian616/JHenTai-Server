package monster.jhentai.interceptor;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import monster.jhentai.cons.Consts;
import monster.jhentai.model.bo.JHenTaiUser;
import monster.jhentai.threadlocal.JHenTaiUserThreadLocal;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;

/**
 * @author JTMonster
 * @date 2024/5/25
 */
@Component
@Slf4j
public class JHenTaiUserInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return true;
        }

        try {
            String memberId = Arrays.stream(cookies).filter(cookie -> Consts.MEMBER_ID_COOKIE.equals(cookie.getName())).findFirst().map(Cookie::getValue).orElse(null);
            String passHash = Arrays.stream(cookies).filter(cookie -> Consts.PASS_HASH_COOKIE.equals(cookie.getName())).findFirst().map(Cookie::getValue).orElse(null);
            if (memberId != null && passHash != null) {
                JHenTaiUserThreadLocal.set(JHenTaiUser.builder().memberId(memberId).passHash(passHash).build());
            }
        } catch (Exception e) {
            log.info("JHenTaiUserInterceptor.preHandle error, cookies:{}", cookies, e);
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        JHenTaiUserThreadLocal.remove();
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        JHenTaiUserThreadLocal.remove();
    }
}
