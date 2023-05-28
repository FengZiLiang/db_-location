package cn.td.aiot.location.controller;

import cn.td.aiot.common.annotation.Log;
import cn.td.aiot.common.controller.BaseController;
import cn.td.aiot.common.exception.TdException;
import cn.td.aiot.location.domain.EnclosureCharacter;
import cn.td.aiot.location.service.EnclosureCharacterService;
import cn.td.aiot.location.vo.EnclosureCharacterVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * ClassName  EnclosureCharacterMapper <br/>
 * Description 电子围栏特性  <br/>
 *
 * @author YeYouGui
 * @version 1.0
 * @date 2020/8/5 16:43<br/>
 * @since JDK 1.8
 */
@RequestMapping("/enclosure/character")
@RestController
@Slf4j
@Validated
public class EnclosureCharacterController extends BaseController {
    private final EnclosureCharacterService enclosureCharacterService;
    private String message;

    public EnclosureCharacterController(EnclosureCharacterService enclosureCharacterService) {
        this.enclosureCharacterService = enclosureCharacterService;
    }

    /**
     * 添加电子围栏特性
     *
     * @param feature 特性
     */
    @Log("添加电子围栏特征")
    @PostMapping
    @RequiresPermissions("enclosure:all")
    public void addCharacter(@Valid EnclosureCharacter feature) throws TdException {
        try {
            enclosureCharacterService.save(feature);
        } catch (Exception e) {
            message = "添加电子围栏特性失败";
            log.error(message, e);
            throw new TdException(message);
        }
    }

    /**
     * 删除电子围栏特性
     *
     * @param id id
     */
    @Log("删除电子围栏特征")
    @DeleteMapping("/{id}")
    @RequiresPermissions("enclosure:all")
    public void deleteCharacter(@PathVariable Integer id) throws TdException {
        try {
            enclosureCharacterService.removeById(id);
        } catch (Exception e) {
            message = "删除电子围栏特性";
            log.error(message, e);
            throw new TdException(message);
        }
    }

    /**
     * 更新电子围栏特性
     *
     * @param feature 特性
     */
    @Log("更新电子围栏特征")
    @RequiresPermissions("enclosure:all")
    @PutMapping
    public void updateCharacter(@Valid EnclosureCharacter feature) throws TdException {
        try {
            enclosureCharacterService.updateById(feature);
        } catch (Exception e) {
            message = "更新电子围栏特性失败";
            log.error(message, e);
            throw new TdException(message);
        }
    }


    /**
     * 查询这个地图电子围栏特性
     *
     * @return Map<String, Object></>
     */
    @GetMapping("/{mapId}")
    public List<EnclosureCharacterVo> findAllCharacter(@NotNull(message = "地图Id不能为空") @PathVariable Integer mapId) {
        return enclosureCharacterService.findAllCharacter(mapId);
    }

}
