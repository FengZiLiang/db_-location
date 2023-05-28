package cn.td.aiot.location.controller;

import cn.td.aiot.common.annotation.Log;
import cn.td.aiot.common.controller.BaseController;
import cn.td.aiot.common.domain.QueryRequest;
import cn.td.aiot.common.exception.TdException;
import cn.td.aiot.location.domain.Floor;
import cn.td.aiot.location.service.FloorService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * ClassName  FloorController <br/>
 * Description 楼层控制层 <br/>
 *
 * @author YeYouGui
 * @version 1.0
 * @date 2020/7/28 16:27<br/>
 * @since JDK 1.8
 */
@Slf4j
@RestController
@RequestMapping("floors")
@Validated
public class FloorController extends BaseController {

    private final FloorService floorService;
    private String message;

    public FloorController(FloorService floorService) {
        this.floorService = floorService;
    }

    /**
     * 添加楼层
     *
     * @param floor 楼层
     */
    @Log("添加楼层")
    @RequiresPermissions("floor:add")
    @PostMapping
    public void addFloor(@Valid Floor floor) throws TdException {
        try {
            floorService.save(floor);
        } catch (Exception e) {
            message = "添加楼层失败";
            log.error(message, e);
            throw new TdException(message);
        }
    }

    /**
     * 查询所有的楼层
     *
     * @param request 分页请求
     * @return Map<String, Object>
     */
    @RequiresPermissions("floor:view")
    @GetMapping
    public Map<String, Object> findAllFloor(QueryRequest request) {
        return getDataTable(floorService.findAllFloor(request, null));
    }

    /**
     * 获取所有的楼层Id
     *
     * @return List<Map < String, Object>>
     */
    @GetMapping("/ids")
    @RequiresPermissions("floor:view")
    public List<Map<String, Object>> findAllFloorIds() {
        QueryWrapper<Floor> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("floor_id", "floor_name");
        List<Floor> list = floorService.list(queryWrapper);
        List<Map<String, Object>> maps = new ArrayList<>();
        list.forEach(floor -> {
            Integer floorId = floor.getFloorId();
            String floorName = floor.getFloorName();
            Map<String, Object> dataMap = getDataMap(floorId, floorName);
            maps.add(dataMap);
        });
        return maps;
    }

    /**
     * 通过建筑Id查询
     *
     * @param request 分页对象
     * @param buildId 建筑Id
     * @return Map<String, Object>
     */
    @RequiresPermissions("floor:view")
    @GetMapping("/{buildId}")
    public Map<String, Object> findFloorByBuildId(QueryRequest request, @NotNull(message = "{required}") @PathVariable Integer buildId) {
        return getDataTable(floorService.findAllFloor(request, buildId));
    }

    /**
     * 通过floorId修改楼层信息
     *
     * @param floor 楼层
     */
    @Log("修改楼层信息")
    @RequiresPermissions("floor:update")
    @PutMapping
    public void updateFloor(@Valid Floor floor) throws TdException {
        try {
            Integer floorId = floor.getFloorId();
            UpdateWrapper<Floor> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("floor_id", floorId);
            floorService.update(floor, updateWrapper);
        } catch (Exception e) {
            message = "修改楼层失败";
            log.error(message, e);
            throw new TdException(message);
        }
    }


    /**
     * 通过楼层Id删除楼层和对应的地图
     *
     * @param floorId 楼层Id
     */
    @Log("删除楼层")
    @RequiresPermissions("floor:delete")
    @DeleteMapping("/{floorId}")
    public void deleteFloor(@NotNull(message = "{required}") @PathVariable Integer floorId) throws TdException {
        try {
            int i = floorService.deleteFloorAndMap(floorId);
            if (i == 0) {
                message = "删除楼层失败";
                throw new TdException(message);
            }
        } catch (Exception e) {
            message = "删除楼层失败";
            log.error(message);
            throw new TdException(message);
        }
    }
}
