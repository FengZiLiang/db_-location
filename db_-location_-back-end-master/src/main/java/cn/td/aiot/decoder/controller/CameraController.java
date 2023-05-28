package cn.td.aiot.decoder.controller;

import cn.td.aiot.common.annotation.Log;
import cn.td.aiot.common.authentication.JWTUtil;
import cn.td.aiot.common.exception.TdException;
import cn.td.aiot.decoder.service.CameraService;
import cn.td.aiot.decoder.vo.CameraVo;
import cn.td.aiot.system.domain.User;
import cn.td.aiot.system.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import java.util.List;

@RestController
@Validated
@RequestMapping("decoder/camera")
public class CameraController {
    @Autowired
    CameraService cameraService;
    @Autowired
    private UserService userService;
    @Log("查询所有设备列表")
    @RequiresPermissions("camera:view")
    @RequestMapping(method = RequestMethod.GET)
    public List<CameraVo> cameraList() {
        //获取用户
        String userName =  JWTUtil.getUsername((String) SecurityUtils.getSubject().getPrincipal());
        User user = userService.findByName(userName);
        return cameraService.getCamera(user.getUserId());

    }

    @RequiresPermissions("camera:view")
    @Log("通过地图ID获取全部摄像头数据")
    @RequestMapping(value = "{mapid}",method = RequestMethod.GET)
    public List<CameraVo> cameraList(@PathVariable(value = "mapid") Long mapid){
        //获取用户
        String userName =  JWTUtil.getUsername((String) SecurityUtils.getSubject().getPrincipal());
        User user = userService.findByName(userName);
        return cameraService.getCamera(mapid,user.getUserId());
    }


    @RequiresPermissions("camera:add")
    @Log("新增设备")
    @RequestMapping( method = RequestMethod.POST)
    public void addCamer(CameraVo cameraVo) throws TdException {
        //获取用户
        String userName =  JWTUtil.getUsername((String) SecurityUtils.getSubject().getPrincipal());
        User user = userService.findByName(userName);
        cameraVo.setUserId(user.getUserId());
        cameraService.addAndCamera(cameraVo);


    }

    @RequiresPermissions("camera:update")
    @Log("修改设备")
    @RequestMapping(value = "{ids}", method = RequestMethod.PUT)
//    @RequiresPermissions("user:update")
    public void updateCamer(CameraVo cameraVo,@PathVariable("ids") Long ids) throws TdException {
        //获取用户
        String userName =  JWTUtil.getUsername((String) SecurityUtils.getSubject().getPrincipal());
        User user = userService.findByName(userName);        cameraVo.setUserId(user.getUserId());
        cameraVo.setDecoderId(ids);
        cameraService.updateCamera(cameraVo);
    }

    @RequiresPermissions("camera:delete")
    @Log("删除设备")
    @RequestMapping(value = "{ids}", method = RequestMethod.DELETE)
    public void deleteCamer(@NotBlank(message = "{required}") @PathVariable(value = "ids") Long ids) throws TdException {
        //获取用户
        String userName =  JWTUtil.getUsername((String) SecurityUtils.getSubject().getPrincipal());
        User user = userService.findByName(userName);
        cameraService.deleteCamera(ids,user.getUserId());
    }

}
