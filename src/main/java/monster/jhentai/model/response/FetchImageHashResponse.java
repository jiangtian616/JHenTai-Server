package monster.jhentai.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author JTMonster
 * @date 2025/5/14
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FetchImageHashResponse {

    private List<String> hashes;
}
