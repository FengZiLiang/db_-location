package cn.td.aiot.location.domain;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * Description 地图实体
 * @author  YeYouGui
 * @date   2020/7/24 10:01
 */
@Data
@TableName("t_map")
public class LocationMap implements Serializable {

  private static final long serialVersionUID = 5605544810498487983L;
  @TableId
  private Integer mapId;
  private String mapName;
  private Double mapX;
  private Double mapY;
  private Double mapZ;
  private String mapFile;
  private Double floorId;

}
