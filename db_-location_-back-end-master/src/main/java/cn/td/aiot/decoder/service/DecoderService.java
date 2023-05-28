package cn.td.aiot.decoder.service;

import cn.td.aiot.common.exception.TdException;
import cn.td.aiot.decoder.domain.ProbeEntity;
import cn.td.aiot.decoder.vo.DecoderVo;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeoutException;

public interface DecoderService {
    /**
     * 开始使用ffmpeg转码.
     * @return 结果
     */
    String startTranscoding(DecoderVo decoderVo) throws InterruptedException, TimeoutException, TdException, IOException;

    /**
     * 测试rtsp是否可以连接.
     * @param rtspStr rtsp连接字符串
     * @return 结果
     */
    boolean canConnnect(String id,String rtspStr) throws IOException, TimeoutException, InterruptedException, TdException;


    /**
     * 停止所有ffmpeg转码.
     * @return 结果
     */
    boolean stopTranscoding();
    /**
     * 根据ID停止ffmpeg转码.
     * @return 结果
     */
    boolean stopTranscoding(String id);


    /**
     * 通过组播方式扫描海康的设备.
     * @exception IOException IOException
     * @exception InterruptedException InterruptedException
     * @return 设备列表
     */
    List<ProbeEntity> scanHKDevices() throws IOException, InterruptedException;
}
