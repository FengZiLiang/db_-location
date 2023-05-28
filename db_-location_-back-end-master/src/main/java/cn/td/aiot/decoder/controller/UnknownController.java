package cn.td.aiot.decoder.controller;

import cn.td.aiot.common.authentication.JWTUtil;
import cn.td.aiot.common.exception.TdException;
import cn.td.aiot.decoder.domain.EtbaseCamera;
import cn.td.aiot.decoder.domain.ProbeEntity;
import cn.td.aiot.decoder.service.CameraService;
import cn.td.aiot.decoder.service.DecoderService;
import cn.td.aiot.decoder.vo.CameraVo;
import cn.td.aiot.system.domain.User;
import cn.td.aiot.system.service.UserService;
import javafx.scene.Camera;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Slf4j
@Controller
@Validated
@RequestMapping("decoder/manager/unknown")
public class UnknownController {

    @Autowired
    DecoderService decoderService;

    @Autowired
    CameraService cameraService;
    @Autowired
    private UserService userService;

    /**
     * 获取所有未知设备
     * @return 设备信息
     */
    @RequiresPermissions("camera:add")
    @RequestMapping(value = "/", method = RequestMethod.GET)
    @ResponseBody
    public List<ProbeEntity> getAllUnknownDevices() throws TdException {
        //获取用户
        String userName =  JWTUtil.getUsername((String) SecurityUtils.getSubject().getPrincipal());
        User user = userService.findByName(userName);

        List<ProbeEntity> probeEntityList = new ArrayList<>();
        //todo 1目前的设备发现只有海康一种 后续可能会有多种 2要与分配过的设备做对比 根据具备唯一性mac地址来
        List<ProbeEntity> allProbeEntities = null;
        try {
            allProbeEntities = decoderService.scanHKDevices();
        } catch (IOException | InterruptedException e) {
            log.error("搜索海康设备失败:" + e.getMessage());
            e.printStackTrace();
            throw new TdException("搜索海康设备失败");
        }

        //过滤
        for (ProbeEntity probeEntity : allProbeEntities) {
            String mac = probeEntity.getMac();
            String ip = probeEntity.getIpv4Address();
            List<CameraVo> cameras = cameraService.getCameraByMac(mac);
            if (cameras == null || cameras.size()<1) {
                probeEntityList.add(probeEntity);
            } else {
                //todo 逻辑没想好
                for (CameraVo camera:cameras){
                    if (!ip.equals(camera.getCameraInfo().getIp())){
                        //设备ip变掉了 导致未知设备没有出来  所以过滤的时候还要考虑ip的因素
                        probeEntityList.add(probeEntity);
                    }
                }

            }
        }
        return probeEntityList;
    }
}
