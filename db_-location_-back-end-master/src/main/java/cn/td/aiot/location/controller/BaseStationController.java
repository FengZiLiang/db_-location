package cn.td.aiot.location.controller;

import cn.td.aiot.common.annotation.Log;
import cn.td.aiot.common.controller.BaseController;
import cn.td.aiot.common.domain.QueryRequest;
import cn.td.aiot.common.exception.TdException;
import cn.td.aiot.location.domain.BaseStation;
import cn.td.aiot.location.power95.SocketCommClient;
import cn.td.aiot.location.service.BaseStationService;
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
 * ClassName  BaseStationController <br/>
 * Description 基站控制层 <br/>
 *
 * @author YeYouGui
 * @version 1.0
 * @date 2020/7/24 12:00<br/>
 * @since JDK 1.8
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/map")
public class BaseStationController extends BaseController {

    private final BaseStationService baseStationService;
    private final SocketCommClient commClient;
    private String message;

    public BaseStationController(BaseStationService baseStationService, SocketCommClient commClient) {
        this.baseStationService = baseStationService;
        this.commClient = commClient;
    }

    /**
     * 添加基站信息
     *
     * @param baseStation 基站信息
     */
    @Log("新增基站")
    @RequiresPermissions("station:add")
    @PostMapping("stations")
    public void addStation(@Valid BaseStation baseStation) throws TdException {
        try {
            baseStationService.save(baseStation);
        } catch (Exception e) {
            message = "添加基站失败";
            log.error(message, e);
            throw new TdException(message);
        }
    }


    /**
     * 通过mac删除基站信息
     *
     * @param baseMac 基站mac地址
     * @throws TdException <></>
     */
    @Log("删除基站")
    @RequiresPermissions("station:delete")
    @DeleteMapping("/stations/mac/{baseMac}")
    public void deleteStationByMac(@NotBlank(message = "{required}") @PathVariable String baseMac) throws TdException {
        try {
            QueryWrapper<BaseStation> deleteByMac = new QueryWrapper<>();
            deleteByMac.eq("base_mac", baseMac);
            baseStationService.remove(deleteByMac);
        } catch (Exception e) {
            message = "删除基站失败";
            log.error(message, e);
            throw new TdException(message);
        }
    }

    /**
     * 通过mac 修改基站信息
     *
     * @param baseStation 基站信息
     * @throws TdException <br/>
     */
    @Log("修改基站")
    @RequiresPermissions("station:update")
    @PutMapping("/stations")
    public void updateStationByMac(@Valid BaseStation baseStation) throws TdException {
        try {
            String baseMac = baseStation.getBaseMac();
            UpdateWrapper<BaseStation> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("base_mac", baseMac);
            baseStationService.update(baseStation, updateWrapper);
        } catch (Exception e) {
            message = "修改基站信息失败";
            log.error(message, e);
            throw new TdException(message);
        }
    }

    /**
     * 获取所有基站信息
     *
     * @return List<BaseStation></>
     */
    @RequiresPermissions("station:view")
    @GetMapping("/stations")
    public Map<String, Object> getAllBaseStation(QueryRequest queryRequest) {
        return getDataTable(baseStationService.findAllBaseStation(queryRequest, null));
    }


    /**
     * 得到所有基站的mac
     *
     * @return List<Integer> </Integer>
     */
    @RequiresPermissions("station:add")
    @GetMapping("/stations/macs")
    public List<Map<String, Object>> getAllMapsId() {
        List<Map<String, Object>> maps = new ArrayList<>();
        //TODO 通过socket来获取
        List<String> macs = new ArrayList<>();
        macs.add("08AA591DDA06");
        macs.add("246F1290BDDA");
        macs.add("28BC5D0E9F04");
        macs.add("3CCA647E0A9F");
        macs.forEach(str -> {
            Map<String, Object> map = getDataMap(str, str);
            maps.add(map);
        });
        return maps;
    }

    /**
     * 通过地图获取基站信息
     *
     * @param mapId 地图Id
     * @return List<BaseStation></>
     */
    @RequiresPermissions("station:view")
    @GetMapping("/stations/{mapId}")
    public Map<String, Object> getBaseStationsByMapId(@NotBlank(message = "{required}") @PathVariable String mapId, QueryRequest request) {
        return getDataTable(baseStationService.findAllBaseStation(request, mapId));
    }
}
