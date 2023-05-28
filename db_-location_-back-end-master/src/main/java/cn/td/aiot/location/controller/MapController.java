package cn.td.aiot.location.controller;

import cn.td.aiot.common.annotation.Log;
import cn.td.aiot.common.controller.BaseController;
import cn.td.aiot.common.domain.QueryRequest;
import cn.td.aiot.common.exception.TdException;
import cn.td.aiot.location.domain.LocationMap;
import cn.td.aiot.location.service.MapService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * ClassName  MapController <br/>
 * Description 地图控制层 <br/>
 *
 * @author YeYouGui
 * @version 1.0
 * @date 2020/7/27 11:06<br/>
 * @since JDK 1.8
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/map")
public class MapController extends BaseController {

    private final MapService mapService;
    private String message;

    public MapController(MapService mapService) {
        this.mapService = mapService;
    }

    /**
     * 添加地图
     *
     * @param map 地图
     */
    @Log("添加地图")
    @RequiresPermissions("map:add")
    @PostMapping
    public void addMap(@Valid LocationMap map) throws TdException {
        try {
            mapService.save(map);
        } catch (Exception e) {
            message = "添加地图失败";
            log.error(message, e);
            throw new TdException(message);
        }

    }

    /**
     * 删除地图
     *
     * @param mapId 地图
     * @throws TdException <br/>
     */
    @Log("删除地图")
    @RequiresPermissions("map:delete")
    @DeleteMapping("/{mapId}")
    public void deleteMapById(@PathVariable String mapId) throws TdException {
        try {
            QueryWrapper<LocationMap> deleteWrapper = new QueryWrapper<>();
            deleteWrapper.eq("map_id", mapId);
            mapService.remove(deleteWrapper);
        } catch (Exception e) {
            message = "删除地图失败";
            log.error(message, e);
            throw new TdException(message);
        }
    }


    /**
     * 通过地图Id修改地图
     *
     * @param map 地图
     * @throws TdException <br/>
     */
    @Log("修改地图")
    @RequiresPermissions("map:update")
    @PutMapping
    public void updateMap(@Valid LocationMap map) throws TdException {
        try {
            Integer mapId = map.getMapId();
            UpdateWrapper<LocationMap> mapQueryWrapper = new UpdateWrapper<>();
            mapQueryWrapper.eq("map_id", mapId);
            mapService.update(map, mapQueryWrapper);
        } catch (Exception e) {
            message = "修改地图失败";
            throw new TdException(message);
        }

    }

    /**
     * 通过楼层查询地图
     *
     * @return List<LocationMap>
     */
    @RequiresPermissions("map:view")
    @GetMapping("/floor/{floorId}")
    public List<LocationMap> getMapsByFloorId(@NotBlank(message = "{required}") @PathVariable String floorId) {
        QueryWrapper<LocationMap> locationMapQueryWrapper = new QueryWrapper<>();
        locationMapQueryWrapper.eq("floor_id", floorId);
        return mapService.list(locationMapQueryWrapper);
    }

    /**
     * 查询所有的地图
     *
     * @return Map<String, Object />
     */
    @RequiresPermissions("map:view")
    @GetMapping
    public Map<String, Object> getAllMaps(QueryRequest queryRequest) {
        return getDataTable(mapService.findAllMap(queryRequest));
    }

    /**
     * 得到所有地图Id
     *
     * @return List<Integer> </Integer>
     */
    @RequiresPermissions("map:view")
    @GetMapping("/ids")
    public List<Map<String, Object>> getAllMapsId() {

        QueryWrapper<LocationMap> locationMapQueryWrapper = new QueryWrapper<>();
        locationMapQueryWrapper.select("map_id", "map_name");
        List<LocationMap> locationMaps = mapService.list(locationMapQueryWrapper);
        List<Map<String, Object>> mapList = new ArrayList<>();
        locationMaps.forEach(locationMap -> {
            Integer mapId = locationMap.getMapId();
            String mapName = locationMap.getMapName();
            Map<String, Object> map = getDataMap(mapId, mapName);
            mapList.add(map);
        });
        return mapList;
    }

}
