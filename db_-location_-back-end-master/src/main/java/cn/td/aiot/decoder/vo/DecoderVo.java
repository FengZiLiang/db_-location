
package cn.td.aiot.decoder.vo;

import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
@SuppressWarnings("unused")
public class DecoderVo {

    /**
     * 输出的设置
     */
    private Map<String,String> outOptions;
    /**
     * 输出的协议
     */
    private String outProtocol;
    /**
     * 设备ID
     */
    private long srcId;

}
