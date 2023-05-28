package cn.td.aiot.decoder.service;

import cn.td.aiot.decoder.domain.EtbaseCamera;
import cn.td.aiot.decoder.vo.CameraVo;

import java.util.List;

public interface CameraService {

    /**
     * 根据mac地址获取摄像头
     * @param mac mac地址
     * @return
     */
    List<CameraVo> getCameraByMac(String mac);
    /**
     * 获取摄像头
     * @param userid 用户ID
     * @return 摄像头数据
     */
    List<CameraVo> getCamera(Long userid);

    /**
     * 获取摄像头
     * @param mapid 地图ID
     * @param userid 用户ID
     * @return 摄像头数据
     */
    List<CameraVo> getCamera(Long mapid,Long userid);

    /**
     * 添加摄像头
     * @param cameraVo 摄像头数据
     * @return 摄像头ID
     */
    Long addAndCamera(CameraVo cameraVo);
    /**
     * 修改摄像头
     * @param cameraVo 摄像头数据
     * @return 摄像头ID
     */
    Long updateCamera(CameraVo cameraVo);

    /**
     * 删除摄像头
     * @param decoderId 摄像头ID
     * @param userid 用户ID
     */
    void deleteCamera(Long decoderId,Long userid);

}
