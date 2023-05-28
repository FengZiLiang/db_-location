package cn.td.aiot.location.controller;

import cn.td.aiot.common.annotation.Log;
import cn.td.aiot.common.controller.BaseController;
import cn.td.aiot.common.domain.QueryRequest;
import cn.td.aiot.common.exception.TdException;
import cn.td.aiot.location.domain.Park;
import cn.td.aiot.location.service.ParkService;
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
 * ClassName  ParkController <br/>
 * Description 园区控制层  <br/>
 *
 * @author YeYouGui
 * @version 1.0
 * @date 2020/7/27 17:49<br/>
 * @since JDK 1.8
 */
@Slf4j
@RestController
@Validated
@RequestMapping("parks")
public class ParkController extends BaseController {
    private final ParkService parkService;
    private String message;

    public ParkController(ParkService parkService) {
        this.parkService = parkService;
    }


    /**
     * 添加园区
     *
     * @param park 园区对象
     * @throws TdException <br/>
     */
    @Log("添加园区")
    @RequiresPermissions("park:add")
    @PostMapping
    public void addPark(@Valid Park park) throws TdException {
        try {
            parkService.save(park);
        } catch (Exception e) {
            message = "添加园区失败";
            throw new TdException(message);
        }
    }

    /**
     * 查询所有园区信息
     *
     * @param request 分页请求
     * @return Map<String, Object>
     */
    @RequiresPermissions("park:view")
    @GetMapping
    public Map<String, Object> getAllPark(QueryRequest request) {
        return getDataTable(parkService.findAllPark(request));
    }


    /**
     * 获取所有园区的Id和名字
     *
     * @return List<Map < String, Object>>
     */
    @RequiresPermissions("park:view")
    @GetMapping("/ids")
    public List<Map<String, Object>> getAllParkId() {
        QueryWrapper<Park> parkQueryWrapper = new QueryWrapper<>();
        parkQueryWrapper.select("park_id", "park_name");

        List<Park> parks = parkService.list(parkQueryWrapper);

        List<Map<String, Object>> mapList = new ArrayList<>();

        parks.forEach(park -> {
            Integer parkId = park.getParkId();
            String parkName = park.getParkName();
            Map<String, Object> dataMap = getDataMap(parkId, parkName);
            mapList.add(dataMap);
        });
        return mapList;
    }


    /**
     * 通过Id查询园区
     *
     * @param parkId 园区Id
     * @return Park
     */
    @RequiresPermissions("park:view")
    @GetMapping("/{parkId}")
    public Park getParkById(@NotBlank(message = "{required}") @PathVariable String parkId) {
        QueryWrapper<Park> parkQueryWrapper = new QueryWrapper<>();
        parkQueryWrapper.eq("park_id", parkId);
        return parkService.getOne(parkQueryWrapper);
    }

    /**
     * 更新园区信息
     *
     * @param park 园区
     */
    @Log("更新园区")
    @RequiresPermissions("park:update")
    @PutMapping
    public void updatePark(@Valid Park park) throws TdException {
        try {
            Integer parkId = park.getParkId();
            UpdateWrapper<Park> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("park_id", parkId);
            parkService.update(park, updateWrapper);
        } catch (Exception e) {
            message = "修改园区失败";
            log.error(message);
            throw new TdException(message);
        }
    }

    /**
     * 通过Id删除园区
     *
     * @param parkId 园区id
     */
    @Log("删除园区")
    @RequiresPermissions("park:delete")
    @DeleteMapping("/{parkId}")
    public void deletePark(@NotBlank(message = "{required}") @PathVariable String parkId) throws TdException {
        try {
            QueryWrapper<Park> parkQueryWrapper = new QueryWrapper<>();
            parkQueryWrapper.eq("park_id", parkId);
            parkService.remove(parkQueryWrapper);
        } catch (Exception e) {
            message = "删除园区失败";
            log.error(message, e);
            throw new TdException(message);
        }
    }

}
