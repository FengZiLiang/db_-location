package cn.td.aiot.location.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.google.gson.JsonObject;
import lombok.Data;

import java.io.Serializable;
import java.lang.reflect.Type;


/**
 * @author YeYouGui
 * 电子围栏规则
 */
@Data
@TableName("t_enclosure_rule")
public class EnclosureRule  implements Serializable {

    private static final long serialVersionUID = -8306524273979313632L;
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer enclosureId;
    private String characterType;
    private String forPeople;
    private Boolean listType;
    private Boolean isForever;
    private String expireTime;
    private String remark;


}
