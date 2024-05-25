package monster.jhentai.config;

import monster.jhentai.interceptor.JHenTaiUserInterceptor;
import monster.jhentai.interceptor.LoginRequiredInterceptor;
import monster.jhentai.interceptor.RateLimitInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author JTMonster
 * @date 2024/5/25
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private RateLimitInterceptor rateLimitInterceptor;
    @Autowired
    private JHenTaiUserInterceptor jHenTaiUserInterceptor;
    @Autowired
    private LoginRequiredInterceptor loginRequiredInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(rateLimitInterceptor).addPathPatterns("/**").excludePathPatterns("/static/**").order(0);
        registry.addInterceptor(jHenTaiUserInterceptor).addPathPatterns("/**").excludePathPatterns("/static/**").order(1);
        registry.addInterceptor(loginRequiredInterceptor).addPathPatterns("/**").excludePathPatterns("/static/**").order(2);
    }
}
