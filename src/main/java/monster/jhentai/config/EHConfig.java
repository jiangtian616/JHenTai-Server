package monster.jhentai.config;

import lombok.Data;
import monster.jhentai.model.bo.EHCookie;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author JTMonster
 * @date 2025/5/14
 */
@Configuration
@ConfigurationProperties(prefix = "eh")
@Data
public class EHConfig {

    private EHCookie cookie;
}
