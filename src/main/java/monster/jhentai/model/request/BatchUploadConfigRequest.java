package monster.jhentai.model.request;

import lombok.Data;

import java.util.List;

/**
 * @author JTMonster
 * @date 2024/5/25
 */
@Data
public class BatchUploadConfigRequest {

    private List<UploadConfigRequest> configs;
}
