package monster.jhentai.model.bo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import monster.jhentai.util.MD5Util;

/**
 * @author JTMonster
 * @date 2024/5/25
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JHenTaiUser {

    private String memberId;

    private String passHash;

    public String toMd5() {
        return MD5Util.encode(memberId + passHash);
    }
}
