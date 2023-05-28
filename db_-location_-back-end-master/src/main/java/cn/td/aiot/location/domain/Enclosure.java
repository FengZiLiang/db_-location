package cn.td.aiot.location.domain;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author YeYouGui
 * 电子围栏
 */
@Data
@TableName("t_enclosure")
public class Enclosure implements Serializable {

    private static final long serialVersionUID = -5292387542672505762L;
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String enclosureName;
    private String enclosureLocation;
    private Integer mapId;
    private Integer userId;
    private String remarks;
    private Date createTime = new Date();


}
