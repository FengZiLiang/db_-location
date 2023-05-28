package cn.td.aiot.location.service;

import cn.td.aiot.location.domain.EnclosureCharacter;
import cn.td.aiot.location.vo.EnclosureCharacterVo;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * ClassName  EnclosureCharacterService <br/>
 * Description 电子围栏特性服务层 <br/>
 *
 * @author YeYouGui
 * @version 1.0
 * @date 2020/8/5 14:34<br/>
 * @since JDK 1.8
 */
@DS("location")
public interface EnclosureCharacterService extends IService<EnclosureCharacter> {
    /**
     * 查询围栏特性
     *
     * @param mapId 地图Id
     * @return List<EnclosureCharacterVo>
     */
    List<EnclosureCharacterVo> findAllCharacter(Integer mapId);
}
