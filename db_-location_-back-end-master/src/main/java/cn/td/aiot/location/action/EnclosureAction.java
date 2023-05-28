package cn.td.aiot.location.action;

import cn.td.aiot.location.domain.Location;
import cn.td.aiot.location.domain.Warning;

import java.util.List;
import java.util.Map;

/**
 * ClassName  EnterEnclosure <br/>
 * Description 抽象的进入电子围栏的接口 <br/>
 *
 * @author YeYouGui
 * @version 1.0
 * @date 2020/8/11 9:28<br/>
 * @since JDK 1.8
 */
public interface EnclosureAction {
    /**
     * 抽象的进入触发的事件
     *  @param pot  设备的坐标
     * @param poly 电子围栏的坐标
     *
     * @return
     */
    Warning enterAction(Location pot, List<Map<String, Object>> poly);
}
