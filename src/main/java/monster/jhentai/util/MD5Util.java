package monster.jhentai.util;

import cn.hutool.crypto.digest.MD5;

/**
 * @author JTMonster
 * @date 2024/5/25
 */
public class MD5Util {

    private static final MD5 md5 = MD5.create();

    public static String encode(String str) {
        return md5.digestHex(str);
    }
}
