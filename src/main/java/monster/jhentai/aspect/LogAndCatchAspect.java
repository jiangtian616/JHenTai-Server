package monster.jhentai.aspect;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import monster.jhentai.enums.ErrorCodeEnum;
import monster.jhentai.model.bo.JHenTaiUser;
import monster.jhentai.model.response.Result;
import monster.jhentai.threadlocal.JHenTaiUserThreadLocal;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Aspect
@Component
@Slf4j
public class LogAndCatchAspect {

    @Autowired
    private ObjectMapper objectMapper;

    @Around("((@annotation(monster.jhentai.annotation.LogAndCatch) || @within(monster.jhentai.annotation.LogAndCatch))" +
            " && within(monster.jhentai.controller.*))")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        String methodName = method.getDeclaringClass().getSimpleName() + "." + method.getName();

        JHenTaiUser jHenTaiUser = JHenTaiUserThreadLocal.get() == null ? null : JHenTaiUserThreadLocal.get().safeLog();
        Object[] args = joinPoint.getArgs();

        if (args != null && args.length > 0) {
            try {
                log.info("{}, user: {}, args: {}", methodName, jHenTaiUser, objectMapper.writeValueAsString(args));
            } catch (Exception e) {
                log.warn("Failed to serialize method args: {}", methodName, e);
            }
        }

        Object result;
        try {
            result = joinPoint.proceed();

            try {
                log.info("{}, user: {}, return value: {}", methodName, jHenTaiUser, objectMapper.writeValueAsString(result));
            } catch (Exception e) {
                log.warn("Failed to serialize return value: {}", methodName, e);
            }

            return result;
        } catch (Exception e) {
            log.error("API exception, method: {}, user: {}", methodName, jHenTaiUser, e);

            if (method.getReturnType().isAssignableFrom(Result.class)) {
                return Result.error(e.getMessage());
            } else {
                throw e;
            }
        }
    }
}