package cn.td.aiot.decoder.dao;

import cn.td.aiot.decoder.domain.EtbaseCamera;
import cn.td.aiot.decoder.vo.CameraVo;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;


@DS("decoder")
public interface EtbaseCameraMapper extends BaseMapper<EtbaseCamera> {
    public List<CameraVo> findAllByMapId(Long userId, Long mapId);

    public List<CameraVo> findCameraByMac(String mac);
}
