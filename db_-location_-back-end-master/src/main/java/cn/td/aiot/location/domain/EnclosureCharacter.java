package cn.td.aiot.location.domain;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @author YeYouGui
 * 电子围栏
 */
@Data
@TableName("t_character")
public class EnclosureCharacter implements Serializable {

    private static final long serialVersionUID = -5887901547108252934L;
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer enclosureId;
    private Short characterType;
    private Boolean isForever;


}
