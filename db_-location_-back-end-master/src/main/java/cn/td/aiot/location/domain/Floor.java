package cn.td.aiot.location.domain;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Description: 楼层实体
 *
 * @author YeYouGui
 * @date 2020/7/24 9:57
 */
@Data
@TableName("t_floor")
public class Floor implements Serializable {

    private static final long serialVersionUID = -3386591940591332762L;
    @TableId
    private Integer floorId;
    private String floorName;
    private String floorInfo;
    private Integer buildId;

}
