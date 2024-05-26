package monster.jhentai.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author JTMonster
 */

@Getter
@AllArgsConstructor
public enum ConfigTypeEnum {
    SETTINGS(1, "settings"),

    BLOCK_RULES(2, "blockRules"),

    HISTORY(3, "histories"),
    ;
    private int code;

    private String name;
    
    public static ConfigTypeEnum getByCode(int code) {
        for (ConfigTypeEnum value : values()) {
            if (value.getCode() == code) {
                return value;
            }
        }
        return null;
    }
}
