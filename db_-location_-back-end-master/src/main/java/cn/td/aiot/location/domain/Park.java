package cn.td.aiot.location.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * Description 园区实体
 *
 * @author YeYouGui
 * @date 2020/7/24 10:02
 */
@Data
@TableName("t_park")
public class Park implements Serializable {

    private static final long serialVersionUID = 4646563752450091458L;

    @TableId
    private Integer parkId;
    private String parkName;
    private String parkProvince;
    private String parkCity;
    private String parkAddress;
    private String parkInfo;

}
