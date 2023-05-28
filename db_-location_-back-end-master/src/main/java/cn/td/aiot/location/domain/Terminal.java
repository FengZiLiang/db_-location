package cn.td.aiot.location.domain;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * Description 定位终端
 *
 * @author YeYouGui
 * @date 2020/7/24 9:56
 */
@Data
@TableName("t_device")
public class Terminal implements Serializable {

    private static final long serialVersionUID = -168250959381506201L;
    @TableId
    private String deviceMac;
    private Integer deviceType;
    private Integer userId;

}
