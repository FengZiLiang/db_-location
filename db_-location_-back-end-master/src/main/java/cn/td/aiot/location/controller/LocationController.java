package cn.td.aiot.location.controller;

import cn.td.aiot.common.exception.TdException;
import cn.td.aiot.location.domain.Location;
import cn.td.aiot.location.service.LocationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * ClassName  LocationController <br/>
 * Description 位置控制层 <br/>
 *
 * @author YeYouGui
 * @version 1.0
 * @date 2020/7/30 9:34<br/>
 * @since JDK 1.8
 */
@Slf4j
@RequestMapping("location")
@Validated
@RestController
public class LocationController {
    private final LocationService locationService;

    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }


    /**
     * 轨迹回放
     *
     * @param userId    查看用户Id
     * @param mapId     查看的地图
     * @param beginTime 开始时间
     * @param endTime   结束时间
     * @return List<Location></br>
     */
    @GetMapping("/playBack/{mapId}")
    public List<Location> locationTrackPlayBack(@NotNull Integer userId,
                                                @NotNull @PathVariable Integer mapId,
                                                @NotBlank String beginTime,
                                                @NotBlank String endTime) throws TdException {


        try {
            return locationService.findHistoryLocation(userId, mapId, beginTime, endTime);
        } catch (TdException e) {
            throw new TdException("查询失败");
        }
    }
}
