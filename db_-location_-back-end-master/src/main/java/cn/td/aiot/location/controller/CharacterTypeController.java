package cn.td.aiot.location.controller;

import cn.td.aiot.common.annotation.Log;
import cn.td.aiot.common.controller.BaseController;
import cn.td.aiot.common.domain.QueryRequest;
import cn.td.aiot.common.exception.TdException;
import cn.td.aiot.location.domain.CharacterType;
import cn.td.aiot.location.service.CharacterTypeService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Map;

/**
 * ClassName  CharacterTypeController <br/>
 * Description 电子围栏标识  <br/>
 *
 * @author YeYouGui
 * @version 1.0
 * @date 2020/8/6 10:55<br/>
 * @since JDK 1.8
 */
@RestController
@Validated
@Slf4j
@RequestMapping("enclosure/character/type")
public class CharacterTypeController extends BaseController {
    private final CharacterTypeService typeService;
    private String message;

    public CharacterTypeController(CharacterTypeService typeService) {
        this.typeService = typeService;
    }


    /**
     * 添加围栏标识
     *
     * @param type 围栏特性
     * @throws TdException <br/>
     */
    @PostMapping
    @RequiresPermissions("enclosure:all")
    @Log("添加围栏特性类型")
    public void addCharacterType(@Valid CharacterType type) throws TdException {
        try {
            typeService.save(type);
        } catch (Exception e) {
            if (e instanceof DuplicateKeyException) {
                message = "字段characterType重复";
            } else {
                message = "添加电子围栏特性类型失败";
            }
            log.error(message, e);
            throw new TdException(message);
        }
    }

    /**
     * 更新电子围栏标识
     *
     * @param type 围栏特性
     * @throws TdException <br/>
     */
    @PutMapping
    @RequiresPermissions("enclosure:all")
    @Log("更新围栏特性类型")
    public void updateCharacterType(@Valid CharacterType type) throws TdException {
        try {
            typeService.updateById(type);
        } catch (Exception e) {
            message = "更新电子围栏特性类型失败";
            log.error(message, e);
            throw new TdException(message);
        }

    }

    /**
     * 删除电子围栏标识
     *
     * @param id 电子围栏标识Id
     * @throws TdException
     */
    @DeleteMapping("/{id}")
    @RequiresPermissions("enclosure:all")
    @Log("删除围栏特性类型")
    public void deleteCharacterType(@NotNull(message = "id不能为空") @PathVariable Integer id) throws TdException {
        try {
            typeService.removeById(id);
        } catch (Exception e) {
            message = "删除围栏特性类型失败";
            log.error(message, e);
            throw new TdException(message);
        }
    }

    /**
     * 查询所有的电子围栏标识
     *
     * @return List<CharacterType></>
     */
    @GetMapping
    @RequiresPermissions("enclosure:all")
    public Map<String, Object> findAllCharacterType(QueryRequest request) {
        Page<CharacterType> page = new Page<>(request.getPageNum(), request.getPageSize());
        return getDataTable(typeService.page(page, null));
    }

}
