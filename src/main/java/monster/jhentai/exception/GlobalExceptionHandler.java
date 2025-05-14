package monster.jhentai.exception;

import lombok.extern.slf4j.Slf4j;
import monster.jhentai.enums.ErrorCodeEnum;
import monster.jhentai.model.response.Result;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Global Exception Handler
 *
 * @author JTMonster
 * @date 2024/6/25
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<?> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        String errorMsg = errors.entrySet().stream()
                .map(entry -> entry.getKey() + ": " + entry.getValue())
                .collect(Collectors.joining(", "));

        log.error("{}.{} invalid params: {}",
                ex.getParameter().getDeclaringClass().getSimpleName(),
                ex.getParameter().getMethod().getName(),
                ex.getTarget()
        );
        return Result.error(ErrorCodeEnum.PARAM_ERROR.getCode(), errorMsg);
    }
} 