package monster.jhentai.interceptor;

import com.alibaba.fastjson.JSON;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import monster.jhentai.annotation.LoginRequired;
import monster.jhentai.constants.EHConsts;
import monster.jhentai.enums.ErrorCodeEnum;
import monster.jhentai.model.bo.JHenTaiUser;
import monster.jhentai.model.response.Result;
import monster.jhentai.service.HTTPService;
import monster.jhentai.threadlocal.JHenTaiUserThreadLocal;
import monster.jhentai.util.IpUtil;
import monster.jhentai.util.parser.LoginPageParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author JTMonster
 * @date 2024/5/25
 */
@Component
@Slf4j
public class LoginRequiredInterceptor implements HandlerInterceptor {

    @Autowired
    private HTTPService httpService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        try {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            if (handlerMethod.hasMethodAnnotation(LoginRequired.class) || handlerMethod.getBeanType().isAnnotationPresent(LoginRequired.class)) {
                JHenTaiUser jHenTaiUser = JHenTaiUserThreadLocal.get();

                if (jHenTaiUser == null) {
                    log.warn("LoginRequiredInterceptor preHandle failed, user not login, uri:{}, ip:{}", request.getRequestURI(), IpUtil.getClientIpAddress(request));
                    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                    JSON.writeJSONString(response.getOutputStream(), Result.error(ErrorCodeEnum.UNAUTHORIZED.getCode(), ErrorCodeEnum.UNAUTHORIZED.getMessage()));
                    response.getOutputStream().flush();
                    return false;
                }

                Result<JHenTaiUser> result = checkUserValid(jHenTaiUser);
                if (!result.isSuccess() || result.getData() == null) {
                    log.warn("LoginRequiredInterceptor preHandle failed, user is invalid, uri:{}, ip:{}", request.getRequestURI(), IpUtil.getClientIpAddress(request));
                    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                    JSON.writeJSONString(response.getOutputStream(), Result.error(ErrorCodeEnum.UNAUTHORIZED.getCode(), "Invalid cookie"));
                    response.getOutputStream().flush();
                    return false;
                }

                JHenTaiUserThreadLocal.set(result.getData());
            }
        } catch (Exception e) {
            log.error("LoginRequiredInterceptor error", e);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            JSON.writeJSONString(response.getOutputStream(), Result.error(ErrorCodeEnum.UNAUTHORIZED.getCode(), "Check cookie failed"));
            response.getOutputStream().flush();
            return false;
        }

        return true;
    }

    private Result<JHenTaiUser> checkUserValid(JHenTaiUser jHenTaiUser) throws IOException {
        String forumUrl = EHConsts.EForums;

        Map<String, String> headers = new HashMap<>();
        headers.put("Cookie", String.format("%s=%s; %s=%s",
                EHConsts.MEMBER_ID_COOKIE, jHenTaiUser.getIpbMemberId(),
                EHConsts.PASS_HASH_COOKIE, jHenTaiUser.getIpbPassHash())
        );

        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("showuser", jHenTaiUser.getIpbMemberId());

        String userName = httpService.get(forumUrl, headers, queryParams, new LoginPageParser());

        if (userName == null) {
            return Result.error();
        }

        JHenTaiUser validatedUser = JHenTaiUser.builder()
                .ipbMemberId(jHenTaiUser.getIpbMemberId())
                .ipbPassHash(jHenTaiUser.getIpbPassHash())
                .userName(userName)
                .build();

        return Result.success(validatedUser);
    }
}
