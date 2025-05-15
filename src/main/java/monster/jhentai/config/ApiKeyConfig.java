package monster.jhentai.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * @author JTMonster
 * @date 2025/5/15
 */
@Configuration
@ConfigurationProperties(prefix = "jh")
@Data
public class ApiKeyConfig {

    private Map<String, String> appSecrets;
}
