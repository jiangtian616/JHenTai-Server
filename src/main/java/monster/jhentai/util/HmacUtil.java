package monster.jhentai.util;

/**
 * @author JTMonster
 * @date 2025/5/15
 */

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class HmacUtil {
    
    public static String hmacSha256(String data, String secretKey) throws Exception {
        Mac mac = Mac.getInstance("HmacSHA256");
        SecretKeySpec key = new SecretKeySpec(secretKey.getBytes(), "HmacSHA256");
        mac.init(key);
        byte[] result = mac.doFinal(data.getBytes());
        return Base64.getEncoder().encodeToString(result);
    }
}
