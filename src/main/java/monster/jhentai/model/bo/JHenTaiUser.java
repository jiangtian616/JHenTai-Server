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

    private String ipbMemberId;

    private String ipbPassHash;

    private String userName;

    public String toMd5() {
        return MD5Util.encode(ipbMemberId + ipbPassHash);
    }

    public JHenTaiUser copy() {
        return JHenTaiUser.builder()
                .ipbMemberId(ipbMemberId)
                .ipbPassHash(ipbPassHash)
                .build();
    }

    public JHenTaiUser safeLog() {
        return JHenTaiUser.builder()
                .userName(userName)
                .build();
    }
}
