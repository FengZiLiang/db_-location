package cn.td.aiot.location.handler;


import cn.td.aiot.location.action.EnclosureAction;
import cn.td.aiot.location.domain.Action;
import cn.td.aiot.location.domain.Enclosure;
import cn.td.aiot.location.domain.Location;
import cn.td.aiot.location.domain.Warning;
import cn.td.aiot.location.service.EnclosureService;
import cn.td.aiot.location.util.EnclosureUtil;
import cn.td.aiot.system.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * ClassName  EnterEnclosureHandler <br/>
 * Description 禁止进入电子围栏区域处理类 <br/>
 *
 * @author YeYouGui
 * @version 1.0
 * @date 2020/8/11 9:48<br/>
 * @since JDK 1.8
 */
@Service
public class EnclosureActionHandler implements EnclosureAction {

    @Autowired
    UserService userService;

    @Autowired
    Gson gson;
    @Autowired
    EnclosureService enclosureService;

    /**
     * @param pot  设备的坐标
     * @param poly 电子围栏的坐标
     * @return TriggerActionVo 触发动作对象
     */
    @Override
    public Warning enterAction(Location pot, List<Map<String, Object>> poly) {
        boolean isEnter = EnclosureUtil.isEnter(pot, poly);
        if (isEnter) {
            QueryWrapper<Enclosure> enclosureQueryWrapper = new QueryWrapper<>();
            // 查询电子围栏的名字
            enclosureQueryWrapper.select("enclosure_name");
            enclosureQueryWrapper.eq("enclosure_location", gson.toJson(poly));
            String enclosureName = enclosureService.getOne(enclosureQueryWrapper).getEnclosureName();
            // 查询用户信息
            Long userId = Long.valueOf(pot.getUserId());
            String mac = pot.getMac();
            String username = userService.getById(userId).getUsername();
            // 创建触发时间的对象
            return Warning.builder()
                    .userId(userId)
                    .username(username)
                    .deviceMac(mac)
                    .location(enclosureName)
                    .createTime(new Date())
                    .actionName(Action.ILLEGAL_ENTER.getActionName())
                    .actionType(Action.ILLEGAL_ENTER.getStatus()).build();
        }
        return null;
    }
}
