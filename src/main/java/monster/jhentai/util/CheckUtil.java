package monster.jhentai.util;

import monster.jhentai.exception.CheckArgumentException;

/**
 * @author JTMonster
 * @date 2024/5/25
 */
public class CheckUtil {

    static public void checkArgument(boolean expression, String errorMessageTemplate) {
        if (!expression) {
            throw new CheckArgumentException(errorMessageTemplate);
        }
    }
}
