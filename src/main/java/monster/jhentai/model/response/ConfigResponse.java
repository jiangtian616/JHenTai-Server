package monster.jhentai.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import monster.jhentai.model.response.vo.ConfigVO;


/**
 * @author JTMonster
 * @date 2024/5/25
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ConfigResponse {

    ConfigVO config;
}
