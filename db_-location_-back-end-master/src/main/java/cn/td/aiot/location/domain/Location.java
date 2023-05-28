package cn.td.aiot.location.domain;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * Description: 位置信息实体
 *
 * @author YeYouGui
 * @date 2020/7/24 9:58
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@TableName("t_location")
public class Location implements Serializable {

    private static final long serialVersionUID = 6445700895144946903L;
    @TableId(type = IdType.INPUT)
    private Integer userId;
    private Double locationX;
    private Double locationY;
    private Double locationZ;
    private Integer mapId;
    private Date locationTime;

    @TableField(exist = false)
    private String mac;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Location)) return false;
        Location location = (Location) o;
        return Objects.equals(userId, location.userId) &&
                Objects.equals(locationX, location.locationX) &&
                Objects.equals(locationY, location.locationY) &&
                Objects.equals(locationZ, location.locationZ) &&
                Objects.equals(mapId, location.mapId) &&
                Objects.equals(locationTime, location.locationTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, locationX, locationY, locationZ, mapId, locationTime);
    }
}
