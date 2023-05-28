package cn.td.aiot.decoder.domain;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("t_user_decoder")
public class UserDecoder {
    /**
     *
     */
    @TableId
    private Long id;
    /**
     * 解码器ID
     */
    private Long decoderId;
    /**
     * 用户ID
     */
    private Long userId;
    /**
     * 原协议
     */
    private String protocol;
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
}
