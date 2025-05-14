package monster.jhentai.enums;

import lombok.Getter;

/**
 * @author JTMonster
 */

@Getter
public enum ErrorCodeEnum {

    SUCCESS(0, "Success"),
    INTERNAL_SERVER_ERROR(-1, "Internal server error"),
    PARAM_ERROR(1, "Param error"),
    UNAUTHORIZED(2, "Unauthorized"),
    ;

    private final int code;
    private final String message;

    ErrorCodeEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
