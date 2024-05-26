package monster.jhentai.model.response.vo;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author JTMonster
 * @date 2024/5/25
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ConfigVO {

    private Long id;
    
    private String shareCode;

    private String identificationCode;

    private Integer type;

    private String version;

    private String config;

    private Long ctime;
}
