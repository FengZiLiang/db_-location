package cn.td.aiot.location.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * ClassName  Warning <br/>
 * Description 报警表 <br/>
 *
 * @author YeYouGui
 * @version 1.0
 * @date 2020/8/21 11:11<br/>
 * @since JDK 1.8
 */
@Data
@Builder
@TableName("t_warning")
public class Warning implements Serializable {
    private static final long serialVersionUID = -4294568507531387722L;
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private String username;
    private String deviceMac;
    private String actionName;
    private Integer actionType;
    private String location;
    private Date createTime;

}
