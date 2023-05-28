package cn.td.aiot.location.controller;

import cn.td.aiot.common.annotation.Log;
import cn.td.aiot.common.exception.TdException;
import cn.td.aiot.location.domain.EnclosureRule;
import cn.td.aiot.location.service.EnclosureRuleService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * ClassName  EnclosureRuleController <br/>
 * Description 电子围栏规则控制层 <br/>
 *
 * @author YeYouGui
 * @version 1.0
 * @date 2020/8/6 15:13<br/>
 * @since JDK 1.8
 */
@Slf4j
@RestController
@Validated
@RequestMapping("/enclosure/rule")
public class EnclosureRuleController {

    private final EnclosureRuleService ruleService;

    private String message;

    public EnclosureRuleController(EnclosureRuleService ruleService) {
        this.ruleService = ruleService;
    }

    /**
     * 添加电子围栏规则
     *
     * @param rule 规则
     * @throws TdException <br/>
     */
    @PostMapping
    @RequiresPermissions("enclosure:all")
    @Log("添加电子围栏规则")
    public void addEnclosureRule(@Valid EnclosureRule rule) throws TdException {
        try {
            ruleService.save(rule);
        } catch (Exception e) {
            message = "添加电子围栏规则失败";
            log.error(message, e);
            throw new TdException(message);
        }
    }

    /**
     * 修改电子围栏规则
     *
     * @param rule 需要修改的规则
     * @throws TdException <br/>
     */
    @PutMapping
    @RequiresPermissions("enclosure:all")
    @Log("修改电子围栏规则")
    public void updateEnclosureRule(@Valid EnclosureRule rule) throws TdException {
        try {
            ruleService.updateById(rule);
        } catch (Exception e) {
            message = "修改电子围栏失败";
            log.error(message, e);
            throw new TdException(message);
        }
    }

    /**
     * 删除电子围栏
     *
     * @param id id
     */
    @DeleteMapping("/{id}")
    @RequiresPermissions("enclosure:all")
    @Log("删除电子围栏规则")
    public void deleteEnclosureRule(@PathVariable Integer id) throws TdException {
        try {
            ruleService.removeById(id);
        } catch (Exception e) {
            message = "删除电子围栏规则失败";
            log.error(message, e);
            throw new TdException(message);
        }
    }

    /**
     * 查询规则
     *
     * @param id   围栏Id
     * @param type 围栏类型
     * @return EnclosureRule
     */
    @GetMapping("/{id}/{type}")
    @RequiresPermissions("enclosure:all")
    public EnclosureRule findEnclosureRule(@PathVariable Integer id, @PathVariable Short type) {
        QueryWrapper<EnclosureRule> enclosureRuleQueryWrapper = new QueryWrapper<>();
        enclosureRuleQueryWrapper.eq("enclosure_id", id);
        enclosureRuleQueryWrapper.eq("character_type", type);
        return ruleService.getOne(enclosureRuleQueryWrapper);
    }
}
