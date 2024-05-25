package monster.jhentai.mapper;

import java.util.List;
import monster.jhentai.model.po.ConfigPO;
import monster.jhentai.model.po.ConfigPOExample;
import org.apache.ibatis.annotations.Param;

public interface ConfigPOMapper {
    long countByExample(ConfigPOExample example);

    int deleteByExample(ConfigPOExample example);

    int deleteByPrimaryKey(Long id);

    int insert(ConfigPO row);

    int insertSelective(ConfigPO row);

    List<ConfigPO> selectByExample(ConfigPOExample example);

    ConfigPO selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("row") ConfigPO row, @Param("example") ConfigPOExample example);

    int updateByExample(@Param("row") ConfigPO row, @Param("example") ConfigPOExample example);

    int updateByPrimaryKeySelective(ConfigPO row);

    int updateByPrimaryKey(ConfigPO row);
}