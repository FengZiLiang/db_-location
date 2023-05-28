package cn.td.aiot.location.vo;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * ClassName  EnclosureCharacterVo <br/>
 * Description 电子围栏特性Vo实体  <br/>
 *
 * @author YeYouGui
 * @version 1.0
 * @date 2020/8/5 17:00<br/>
 * @since JDK 1.8
 */
@Data
@Builder
public class EnclosureCharacterVo {


    private Integer enclosureId;
    private String enclosureName;
    private List<List<Map<String, Object>>> features;

}
