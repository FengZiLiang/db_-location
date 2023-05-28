package cn.td.aiot.location.domain;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * DDescription: 基站信息实体
 *
 * @author YeYouGui
 * @date 2020/7/24 9:50
 */
@Data
@TableName("t_base")
public class BaseStation implements Serializable {

    private static final long serialVersionUID = -5931441264705201949L;
    @TableId
    private String baseMac;
    private Double baseX;
    private Double baseY;
    private Double baseZ;
    private Integer mapId;
    private Boolean isMaster;

}
