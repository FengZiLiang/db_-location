package cn.td.aiot.location.controller;

import cn.td.aiot.common.annotation.Log;
import cn.td.aiot.common.controller.BaseController;
import cn.td.aiot.common.domain.QueryRequest;
import cn.td.aiot.common.exception.TdException;
import cn.td.aiot.location.domain.Terminal;
import cn.td.aiot.location.service.TerminalService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * ClassName  TerminalController <br/>
 * Description 设备管理 <br/>
 *
 * @author YeYouGui
 * @version 1.0
 * @date 2020/7/29 15:57<br/>
 * @since JDK 1.8
 */
@Slf4j
@Validated
@RestController
@RequestMapping("devices")
public class TerminalController extends BaseController {
    private final TerminalService terminalService;
    private String message;

    public TerminalController(TerminalService terminalService) {
        this.terminalService = terminalService;
    }


    /**
     * 查询所有的设备
     *
     * @param queryRequest 分页查询参数
     * @return Map<String, Object>
     */
    @GetMapping
    public Map<String, Object> findAllDevices(QueryRequest queryRequest) {
        return getDataTable(terminalService.findAllTerminal(queryRequest, null));
    }

    /**
     * 通过deviceType查询设备
     *
     * @param queryRequest 分页请求参数
     * @param deviceType   设备类型
     * @return Map<String, Object>
     */
    @RequiresPermissions("devices:view")
    @GetMapping("/type/{deviceType}")
    public Map<String, Object> findDeviceByDeviceId(QueryRequest queryRequest, @PathVariable Integer deviceType) {
        return getDataTable(terminalService.findAllTerminal(queryRequest, deviceType));
    }


    /**
     * 通过UserId查询用户设备
     *
     * @param userId 用户Id
     * @return List<Terminal>
     */
    @RequiresPermissions("devices:view")
    @GetMapping("/user/{userId}")
    public List<Terminal> findDeviceByUserId(@PathVariable Integer userId) {
        //根据用户Id查询
        QueryWrapper<Terminal> userIdWrapper = new QueryWrapper<>();
        userIdWrapper.eq("user_id", userId);
        return terminalService.list(userIdWrapper);
    }

    /**
     * 通过deviceType 和 userId 查询设备
     *
     * @param deviceType 设备类型（0:工牌； 1:手环；2:手表）
     * @param userId     用户id
     * @return Map<String, Object>
     */
    @RequiresPermissions("devices:view")
    @GetMapping("/user/{userId}/type/{deviceType}")
    public Terminal findDeviceByDeviceIdAndUserId(@PathVariable Integer deviceType,
                                                  @PathVariable Integer userId) {
        QueryWrapper<Terminal> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        queryWrapper.eq("device_type", deviceType);
        return terminalService.getOne(queryWrapper);
    }


    /**
     * 添加设备
     *
     * @param terminal 终端
     * @throws TdException <br/>
     */
    @Log("添加设备")
    @RequiresPermissions("device:add")
    @PostMapping
    public void addDevice(@Valid Terminal terminal) throws TdException {
        try {
            terminalService.save(terminal);
        } catch (Exception e) {
            message = " 添加设备失败";
            log.error(message, e);
            throw new TdException(message);
        }
    }


    /**
     * 通过设备mac修改信息
     *
     * @param terminal 终端
     * @throws TdException <br/>
     */
    @Log("修改设备信息")
    @RequiresPermissions("device:update")
    @PutMapping
    public void updateDevice(@Valid Terminal terminal) throws TdException {
        try {
            String deviceMac = terminal.getDeviceMac();
            UpdateWrapper<Terminal> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("device_mac", deviceMac);
            terminalService.update(terminal, updateWrapper);
        } catch (Exception e) {
            message = "修改设备信息失败";
            log.error(message, e);
            throw new TdException(message);
        }
    }


    /**
     * 通过mac删除设备
     *
     * @param deviceMac 设备mac
     * @throws TdException <br/>
     */
    @Log("删除设备")
    @RequiresPermissions("device:delete")
    @DeleteMapping("/{deviceMac}")
    public void deleteDevice(@PathVariable String deviceMac) throws TdException {
        try {
            QueryWrapper<Terminal> deleteWrapper = new QueryWrapper<>();
            deleteWrapper.eq("device_mac", deviceMac);
            terminalService.remove(deleteWrapper);
        } catch (Exception e) {
            message = "删除设备失败";
            log.error(message, e);
            throw new TdException(message);
        }
    }
}
