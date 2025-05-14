package monster.jhentai.model.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @author JTMonster
 * @date 2025/5/14
 */
@Data
public class FetchImageHashRequest {

    @NotNull
    @Min(1)
    private Integer gid;

    @NotBlank
    private String token;
}
