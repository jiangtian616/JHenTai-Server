package monster.jhentai.model.request;

import lombok.Data;

/**
 * @author JTMonster
 * @date 2024/5/25
 */
@Data
public class UploadConfigRequest {

    private Integer type;

    private String version;
    
    private String config;
}
