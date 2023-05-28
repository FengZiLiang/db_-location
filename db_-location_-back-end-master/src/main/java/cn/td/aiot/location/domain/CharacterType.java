package cn.td.aiot.location.domain;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @author YeYouGui
 * 电子围栏特性类型
 */
@Data
@TableName("t_character_type")
public class CharacterType implements Serializable {

    private static final long serialVersionUID = 5556028967631273220L;
    @TableId(type =IdType.AUTO)
    private Integer id;
    private Short characterType;
    private String characterName;


}
