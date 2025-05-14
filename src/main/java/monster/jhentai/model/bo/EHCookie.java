package monster.jhentai.model.bo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

/**
 * @author JTMonster
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EHCookie {

    private Integer ipbMemberId;

    private String ipbPassHash;

    private String igneous;

    public boolean isEHValid() {
        return ipbMemberId != null && StringUtils.isNoneBlank(ipbPassHash);
    }

    public boolean isEXValid() {
        return ipbMemberId != null && StringUtils.isNoneBlank(ipbPassHash) && StringUtils.isNoneBlank(igneous);
    }
} 