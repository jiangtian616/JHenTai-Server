package monster.jhentai.biz;


import lombok.extern.slf4j.Slf4j;
import monster.jhentai.model.bo.JHenTaiUser;
import monster.jhentai.model.po.ConfigPO;
import monster.jhentai.model.request.BatchUploadConfigRequest;
import monster.jhentai.model.request.ListConfigRequest;
import monster.jhentai.model.response.ConfigResponse;
import monster.jhentai.model.response.ListConfigResponse;
import monster.jhentai.model.response.UploadConfigResponse;
import monster.jhentai.model.response.vo.ConfigVO;
import monster.jhentai.service.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


/**
 * @author JTMonster
 * @date 2024/5/25
 */
@Service
@Slf4j
public class ConfigBiz {
    @Autowired
    private ConfigService configService;

    public ListConfigResponse list(ListConfigRequest request, JHenTaiUser user) {
        String identificationCode = user.toMd5();

        List<ConfigPO> configPOS = configService.listConfig(request.getType(), identificationCode, 50);

        List<ConfigVO> configVOS = configPOS.stream()
                .map(this::convert2ConfigVO)
                .toList();

        return new ListConfigResponse(configVOS);
    }

    public ConfigResponse getByShareCode(String shareCode) {
        ConfigPO configPO = configService.getByShareCode(shareCode);

        return new ConfigResponse(convert2ConfigVO(configPO));
    }

    public UploadConfigResponse upload(BatchUploadConfigRequest request, JHenTaiUser user) {
        String identificationCode = user.toMd5();

        List<ConfigVO> configVOS = request.getConfigs().stream()
                .map(config -> ConfigVO.builder()
                        .type(config.getType())
                        .version(config.getVersion())
                        .config(config.getConfig())
                        .identificationCode(identificationCode)
                        .shareCode(UUID.randomUUID().toString())
                        .build())
                .toList();
        configService.batchInsertNewConfig(configVOS);

        return new UploadConfigResponse(identificationCode);
    }

    public void deleteConfig(Long id, JHenTaiUser user) {
        String identificationCode = user.toMd5();

        boolean success = configService.deleteConfig(id, identificationCode) > 0;
        if (!success) {
            log.error("ConfigBiz.deleteConfig failed, id:{}, user:{}", id, user);
        }
    }

    private ConfigVO convert2ConfigVO(ConfigPO configPO) {
        if (configPO == null) {
            return null;
        }

        return ConfigVO.builder()
                .id(configPO.getId())
                .type(configPO.getType())
                .version(configPO.getVersion())
                .config(configPO.getConfig())
                .ctime(configPO.getCtime().getTime())
                .identificationCode(configPO.getIdentificationCode())
                .shareCode(configPO.getShareCode())
                .build();
    }
}
