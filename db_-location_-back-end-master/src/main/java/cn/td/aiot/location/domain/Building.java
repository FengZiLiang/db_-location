package cn.td.aiot.location.domain;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Description: 建筑实体
 *
 * @author YeYouGui
 * @date 2020/7/24 9:55
 */
@Data
@TableName("t_build")
public class Building implements Serializable {

    private static final long serialVersionUID = -5783271192276467705L;
    @TableId
    private Integer buildId;
    private String buildName;
    private String buildInfo;
    private Integer parkId;
}
