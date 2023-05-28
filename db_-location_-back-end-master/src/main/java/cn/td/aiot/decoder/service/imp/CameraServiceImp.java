package cn.td.aiot.decoder.service.imp;

import cn.td.aiot.decoder.dao.EtbaseCameraMapper;
import cn.td.aiot.decoder.dao.UserDecoderMapper;
import cn.td.aiot.decoder.domain.EtbaseCamera;
import cn.td.aiot.decoder.domain.UserDecoder;
import cn.td.aiot.decoder.service.CameraService;
import cn.td.aiot.decoder.utils.commandManager.TdFFmpegManagerImpl;
import cn.td.aiot.decoder.vo.CameraVo;
import com.baomidou.dynamic.datasource.annotation.DS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Service
@DS("et_decoder")
public class CameraServiceImp implements CameraService {
    @Autowired
    UserDecoderMapper userDecoderMapper;
    @Autowired
    EtbaseCameraMapper etbaseCameraMapper;

    @Override
    public List<CameraVo> getCameraByMac(String mac) {
        return etbaseCameraMapper.findCameraByMac(mac);
    }

    @Override
    public List<CameraVo> getCamera(Long userid) {
        List<CameraVo> cameraVoList = new LinkedList<>();
        List<UserDecoder> userDecoderList=userDecoderMapper.findAllByUserId(userid);
        for (UserDecoder userDecoder : userDecoderList){

            CameraVo cameraVo=new CameraVo();
            cameraVo.setDecoderId(userDecoder.getDecoderId());
            cameraVo.setProtocol(userDecoder.getProtocol());
            cameraVo.setName(userDecoder.getName());
            cameraVo.setStatus(userDecoder.getStatus());
            cameraVo.setModel(userDecoder.getModel());
            EtbaseCamera etbaseCamera = etbaseCameraMapper.selectById(cameraVo.getDecoderId());
            //摄像机未开启
            short a = 1;
            //摄像机开启
            short b = 2;
            short start = (TdFFmpegManagerImpl
                    .getInstance()
                    .query(String.valueOf(cameraVo.getDecoderId())))==null?a:b;
            etbaseCamera.setCameraStatus(start);
            etbaseCamera.setDecoderId(null);
            cameraVo.setCameraInfo(etbaseCamera);
            cameraVoList.add(cameraVo);
        }
        return cameraVoList;
    }

    @Override
    public List<CameraVo> getCamera(Long mapid, Long userid) {
        List<CameraVo> cameraVoList=  etbaseCameraMapper.findAllByMapId(userid,mapid);
        for (CameraVo cameraVo : cameraVoList){
            //摄像机未开启
            short a = 1;
            //摄像机开启
            short b = 2;
            short start = (TdFFmpegManagerImpl
                    .getInstance()
                    .query(String.valueOf(cameraVo.getDecoderId())))==null?a:b;
            cameraVo.getCameraInfo()
                    .setCameraStatus(start);
            cameraVo.getCameraInfo().setDecoderId(null);
        }
        return cameraVoList;
    }

    @Override
    public Long addAndCamera(CameraVo cameraVo) {
        //是否更新UserDecoder表
        boolean upUserDecoder=true;
        //是否更新UserDecoder表
        boolean upEtbaseCamer=true;
        EtbaseCamera etbaseCamer = (EtbaseCamera) cameraVo.getCameraInfo();
        UserDecoder userDecoder = new UserDecoder();
        etbaseCamer.setDecoderId(cameraVo.getDecoderId());
        if (cameraVo.getDecoderId()!=null){

            if (userDecoderMapper.findAllByUserIdAndDecoderId(cameraVo.getDecoderId(),cameraVo.getUserId())!=null){
                return updateCamera(cameraVo);
            }else {
                upUserDecoder=true;
                upEtbaseCamer=false;
            }

        }

        //插入
        if (upEtbaseCamer){
            etbaseCameraMapper.insert(etbaseCamer);
            cameraVo.setDecoderId(etbaseCamer.getDecoderId());
        }
        if (upUserDecoder) {


            userDecoder.setDecoderId(cameraVo.getDecoderId());
            userDecoder.setModel(cameraVo.getModel());
            userDecoder.setName(cameraVo.getName());
            userDecoder.setProtocol(cameraVo.getProtocol());
            userDecoder.setStatus(cameraVo.getStatus());
            userDecoder.setUserId(cameraVo.getUserId());
            userDecoderMapper.insert(userDecoder);
        }
        return userDecoder.getDecoderId();
    }

    @Override
    public Long updateCamera(CameraVo cameraVo) {
        UserDecoder userDecoder = new UserDecoder();
        userDecoder.setModel(cameraVo.getModel());
        userDecoder.setName(cameraVo.getName());
        userDecoder.setProtocol(cameraVo.getProtocol());
        userDecoder.setStatus(cameraVo.getStatus());
        userDecoder.setUserId(cameraVo.getUserId());
        userDecoder.setDecoderId(cameraVo.getDecoderId());

        userDecoderMapper.updateByUserIdAndDecoderId(userDecoder);
        EtbaseCamera etbaseCamer = (EtbaseCamera) cameraVo.getCameraInfo();
        etbaseCameraMapper.updateById(etbaseCamer);
        return etbaseCamer.getDecoderId();
    }

    @Override
    public void deleteCamera(Long decoderId,Long userid) {

        List<UserDecoder> userDecoderList=userDecoderMapper.findAllByUserIdAndDecoderId(decoderId,userid);
        for (UserDecoder userDecoder:userDecoderList){
            userDecoder.setStatus((short) 0);
            userDecoderMapper.updateById(userDecoder);
        }

    }


}
