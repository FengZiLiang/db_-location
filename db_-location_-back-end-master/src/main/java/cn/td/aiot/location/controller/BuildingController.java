package cn.td.aiot.location.controller;

import cn.td.aiot.common.annotation.Log;
import cn.td.aiot.common.controller.BaseController;
import cn.td.aiot.common.domain.QueryRequest;
import cn.td.aiot.common.exception.TdException;
import cn.td.aiot.location.domain.Building;
import cn.td.aiot.location.service.BuildingService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * ClassName  BuildingController <br/>
 * Description 建筑控制层 <br/>
 *
 * @author YeYouGui
 * @version 1.0
 * @date 2020/7/28 15:02<br/>
 * @since JDK 1.8
 */
@Slf4j
@RestController
@RequestMapping("builds")
@Validated
public class BuildingController extends BaseController {

    private final BuildingService buildingService;
    private String message;

    public BuildingController(BuildingService buildingService) {
        this.buildingService = buildingService;
    }

    /**
     * 查询所有的建筑信息
     *
     * @param queryRequest 分页查询参数
     * @return Map<String, Object>
     */
    @RequiresPermissions("build:view")
    @GetMapping
    public Map<String, Object> findAllBuilding(QueryRequest queryRequest) {
        return getDataTable(buildingService.findAllBuilding(queryRequest, null));
    }

    /**
     * 查询所有的建筑Id
     *
     * @return List<Map < String, Object>>
     */
    @RequiresPermissions("build:view")
    @GetMapping("/ids")
    public List<Map<String, Object>> findAllBuildingId() {
        QueryWrapper<Building> buildingQueryWrapper = new QueryWrapper<>();
        buildingQueryWrapper.select("build_id", "build_name");
        List<Building> list = buildingService.list(buildingQueryWrapper);
        List<Map<String, Object>> maps = new ArrayList<>();
        list.forEach(building -> {
            Integer buildId = building.getBuildId();
            String buildName = building.getBuildName();
            Map<String, Object> dataMap = getDataMap(buildId, buildName);
            maps.add(dataMap);
        });
        return maps;
    }

    /**
     * 通过园区id查询建筑信息
     *
     * @param queryRequest 分页请求参数
     * @param parkId       园区Id
     * @return Map<String, Object>
     */
    @RequiresPermissions("build:view")
    @GetMapping("/{parkId}")
    public Map<String, Object> findBuildingByParkId(QueryRequest queryRequest, @PathVariable Integer parkId) {
        return getDataTable(buildingService.findAllBuilding(queryRequest, parkId));
    }

    /**
     * 添加建筑
     *
     * @param building 建筑信息
     * @throws TdException <br/>
     */
    @Log("添加建筑")
    @RequiresPermissions("build:add")
    @PostMapping
    public void addBuilding(@Valid Building building) throws TdException {
        try {
            buildingService.save(building);
        } catch (Exception e) {
            message = " 添加建筑失败";
            log.error(message, e);
            throw new TdException(message);
        }
    }

    /**
     * 通过建筑Id修改建筑信息
     *
     * @param building 建筑
     */
    @Log("修改建筑信息")
    @RequiresPermissions("build:delete")
    @PutMapping
    public void updateBuilding(@Valid Building building) throws TdException {
        try {
            Integer buildId = building.getBuildId();
            UpdateWrapper<Building> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("build_id", buildId);
            buildingService.update(building, updateWrapper);
        } catch (Exception e) {
            message = "修改建筑信息失败";
            log.error(message, e);
            throw new TdException(message);
        }
    }

    /**
     * 删除建筑
     *
     * @param buildId 建筑Id
     * @throws TdException <br/>
     */
    @Log("删除建筑")
    @RequiresPermissions("build:delete")
    @DeleteMapping("/{buildId}")
    public void deleteBuilding(@PathVariable Integer buildId) throws TdException {
        try {
            QueryWrapper<Building> deleteWrapper = new QueryWrapper<>();
            deleteWrapper.eq("build_id", buildId);
            buildingService.remove(deleteWrapper);
        } catch (Exception e) {
            message = "删除建筑失败";
            log.error(message, e);
            throw new TdException(message);
        }
    }

}
