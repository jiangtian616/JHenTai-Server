package monster.jhentai.biz;

import lombok.extern.slf4j.Slf4j;
import monster.jhentai.config.EHConfig;
import monster.jhentai.constants.EHConsts;
import monster.jhentai.service.HTTPService;
import monster.jhentai.util.parser.EHPageParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author JTMonster
 * @date 2025/5/14
 */
@Component
@Slf4j
public class EHBiz {

    @Autowired
    private EHConfig ehConfig;

    @Autowired
    private HTTPService httpService;

    public <T> T requestMPVPage(Integer gid, String token, EHPageParser<T> ehPageParser) throws IOException {
        if (!ehConfig.getCookie().isEXValid()) {
            throw new IllegalStateException("Server cookie config invalid");
        }

        String mpvUrl = String.format(EHConsts.EMPVTemplate, gid, token);

        Map<String, String> headers = new HashMap<>();
        headers.put("Cookie", String.format("%s=%s; %s=%s; %s=%s",
                EHConsts.MEMBER_ID_COOKIE, ehConfig.getCookie().getIpbMemberId(),
                EHConsts.PASS_HASH_COOKIE, ehConfig.getCookie().getIpbPassHash(),
                EHConsts.IGNEOUS, ehConfig.getCookie().getIgneous())
        );

        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("datatags", "1");

        return httpService.get(mpvUrl, headers, queryParams, ehPageParser);
    }
}
