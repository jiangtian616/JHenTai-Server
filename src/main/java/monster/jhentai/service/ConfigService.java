package monster.jhentai.service;

import monster.jhentai.mapper.ConfigPOMapper;
import monster.jhentai.model.po.ConfigPO;
import monster.jhentai.model.po.ConfigPOExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author JTMonster
 * @date 2024/5/25
 */
@Service
public class ConfigService {

    @Autowired
    private ConfigPOMapper configPOMapper;

    public List<ConfigPO> listConfig(Integer type, String identificationCode) {
        ConfigPOExample configPOExample = new ConfigPOExample();
        ConfigPOExample.Criteria criteria = configPOExample.createCriteria();
        if (type != null) {
            criteria.andTypeEqualTo(type);
        }
        criteria.andIdentificationCodeEqualTo(identificationCode);
        criteria.andIsDeletedEqualTo(false);
        configPOExample.setOrderByClause("ctime desc");

        return configPOMapper.selectByExample(configPOExample);
    }

    public ConfigPO getByShareCode(String shareCode) {
        ConfigPOExample configPOExample = new ConfigPOExample();
        configPOExample.createCriteria().andShareCodeEqualTo(shareCode).andIsDeletedEqualTo(false);
        List<ConfigPO> configPOS = configPOMapper.selectByExample(configPOExample);
        return configPOS.isEmpty() ? null : configPOS.get(0);
    }

    public int insertNewConfig(Integer type, String version, String config, String identificationCode, String shareCode) {
        ConfigPO configPO = new ConfigPO();
        configPO.setType(type);
        configPO.setVersion(version);
        configPO.setConfig(config);
        configPO.setIdentificationCode(identificationCode);
        configPO.setShareCode(shareCode);
        return configPOMapper.insertSelective(configPO);
    }

    public int deleteConfig(Long id, String identificationCode) {
        ConfigPOExample configPOExample = new ConfigPOExample();
        configPOExample.createCriteria().andIdEqualTo(id).andIdentificationCodeEqualTo(identificationCode);

        ConfigPO configPO = new ConfigPO();
        configPO.setIsDeleted(true);
        return configPOMapper.updateByExampleSelective(configPO, configPOExample);
    }
}
