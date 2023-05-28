package cn.td.aiot.decoder.domain;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("t_etbase_camera")
public class EtbaseCamera {
    @TableId
    private Long decoderId;
    /**
     * 设备状态
     */
    private Short cameraStatus;
    /**
     * 设备型号
     */
    private String model;
    /**
     * mac地址
     */
    private String mac;

    private Double indexX;

    private Double indexY;

    private Double indexZ;

    private Integer mapId;


    /**
     *摄像机IP
     */
    private String ip;
    /**
     *摄像机用户名
     */
    private String userName;
    /**
     *摄像机密码
     */
    private String password;
    /**
     *摄像机端口
     */
    private Integer port=554;
}
