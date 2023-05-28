package cn.td.aiot.location.util;

import cn.td.aiot.location.domain.Location;

import java.util.List;
import java.util.Map;

/**
 * ClassName  EnclosureUtil <br/>
 * Description 电子围栏工具类 <br/>
 *
 * @author YeYouGui
 * @version 1.0
 * @date 2020/8/11 9:33<br/>
 * @since JDK 1.8
 */
public class EnclosureUtil {

    /**
     * 判断是否进入电子围栏
     *
     * @param pt   设备的坐标点
     * @param poly 电子围栏的坐标点
     * @return boolean
     */
    public static boolean isEnter(Location pt, List<Map<String, Object>> poly) {


        // 设备的坐标点
        double ptX = pt.getLocationX();
        double ptY = pt.getLocationY();
        int len = poly.size();
        boolean c = false;
        for (int i = -1, j = len - 1; ++i < len; j = i) {
            double ployiy = (double) poly.get(i).get("y");
            double ployix = (double) poly.get(i).get("x");
            double ployjy = (double) poly.get(j).get("y");
            double ployjx = (double) poly.get(j).get("x");
            boolean resij = ((ployiy <= ptY && ptY < ployjy) || (ployjy <= ptY && ptY < ployiy)) && (ptX < (ployjx - ployix) * (ptY - ployiy) / (ployjy - ployiy) + ployix) && (c = !c);

        }
        return c;
    }


}
