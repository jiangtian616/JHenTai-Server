package monster.jhentai.interceptor;

import com.alibaba.fastjson.JSON;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import monster.jhentai.annotation.LoginRequired;
import monster.jhentai.cons.Consts;
import monster.jhentai.model.bo.JHenTaiUser;
import monster.jhentai.model.response.Result;
import monster.jhentai.threadlocal.JHenTaiUserThreadLocal;
import monster.jhentai.util.IpUtil;
import okhttp3.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.concurrent.TimeUnit;


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

                boolean isValidUser = checkUserValid(jHenTaiUser);
                if (!isValidUser) {
                    log.warn("LoginRequiredInterceptor preHandle failed, user is invalid, uri:{}, ip:{}", request.getRequestURI(), IpUtil.getClientIpAddress(request));
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

    private boolean checkUserValid(JHenTaiUser jHenTaiUser) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.setProxy$okhttp(new Proxy(Proxy.Type.HTTP, new InetSocketAddress("localhost", 7890)));
        OkHttpClient client = builder.build();

        HttpUrl.Builder queryUrlBuilder = HttpUrl.get(Consts.EForums).newBuilder();
        queryUrlBuilder.addQueryParameter("showuser", jHenTaiUser.getMemberId());
        String url = queryUrlBuilder.build().toString();

        Request request = new Request.Builder()
                .url(url)
                .header("Cookie", String.format("ipb_member_id=%s; ipb_pass_hash=%s", jHenTaiUser.getMemberId(), jHenTaiUser.getPassHash()))
                .build();

        Call call = client.newCall(request);
        call.timeout().timeout(2000, TimeUnit.MILLISECONDS);
        try (Response response = call.execute()) {
            if (!response.isSuccessful() || response.body() == null) {
                log.info("LoginRequiredInterceptor.checkUserValid failed, response code:{}", response.code());
                return false;
            }

            String html = response.body().string();
            Document document = Jsoup.parse(html);

            if (!document.getElementsByClass("pcen").isEmpty()) {
                return false;
            }

            Elements link = document.select(".home > b > a");
            if (link.isEmpty()) {
                return false;
            }

            String userName = link.text();

            return true;
        } catch (IOException e) {
            log.info("LoginRequiredInterceptor.checkUserValid failed", e);
            return false;
        }
    }
}
