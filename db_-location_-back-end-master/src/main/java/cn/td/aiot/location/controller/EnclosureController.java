package cn.td.aiot.location.controller;

import cn.td.aiot.common.annotation.Log;
import cn.td.aiot.common.controller.BaseController;
import cn.td.aiot.common.domain.QueryRequest;
import cn.td.aiot.common.exception.TdException;
import cn.td.aiot.location.domain.Enclosure;
import cn.td.aiot.location.service.EnclosureService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Map;

/**
 * ClassName  EnclosureController <br/>
 * Description 电子围栏控制层  <br/>
 *
 * @author YeYouGui
 * @version 1.0
 * @date 2020/8/5 14:49<br/>
 * @since JDK 1.8
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/enclosure")
public class EnclosureController extends BaseController {
    private final EnclosureService enclosureService;
    private String message;

    public EnclosureController(EnclosureService enclosureService) {
        this.enclosureService = enclosureService;
    }

    /**
     * 添加电子围栏
     *
     * @param enclosure 电子围栏
     * @throws TdException 创建电子围栏失败异常
     */
    @PostMapping
    @Log("添加电子围栏")
    @RequiresPermissions("enclosure:all")
    public void addEnclosure(@Valid Enclosure enclosure) throws TdException {
        try {
            enclosureService.save(enclosure);
        } catch (Exception e) {
            message = "创建电子围栏失败";
            log.error(message, e);
            throw new TdException(message);
        }
    }

    /**
     * 删除电子围栏
     *
     * @param id 电子围栏Id
     * @throws TdException 删除失败异常
     */
    @DeleteMapping("/{id}")
    @Log("删除电子围栏")
    @RequiresPermissions("enclosure:all")
    public void deleteEnclosure(@NotNull(message = "围栏id不能为空") @PathVariable Integer id) throws TdException {
        try {
            enclosureService.removeById(id);
        } catch (Exception e) {
            log.error(message, e);
            throw new TdException(message);
        }
    }


    /**
     * 查询电子围栏
     *
     * @param queryRequest 分页查询参数
     * @return Map
     */
    @GetMapping
    public Map<String, Object> findAllEnclosure(QueryRequest queryRequest) {
        return getDataTable(enclosureService.findAllEnclosure(queryRequest));
    }


}
