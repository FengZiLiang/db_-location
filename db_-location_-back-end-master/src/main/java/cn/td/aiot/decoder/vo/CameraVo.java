package cn.td.aiot.decoder.vo;

import cn.td.aiot.decoder.domain.EtbaseCamera;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class CameraVo {
    /**
     * 解码器ID
     */
    private Long decoderId;
    /**
     * 原协议
     */
    private String protocol;
    /**
     * 用户ID
     */
    @JsonIgnore
    private Long userId;
    /**
     * 自定义名字
     */
    private String name;
    /**
     * 状态
     */
    private Short status;
    /**
     * 设备型号可有可无
     */
    private String model;

    private EtbaseCamera cameraInfo;


}
