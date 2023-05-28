package cn.td.aiot.location.service.impl;

import cn.td.aiot.common.domain.QueryRequest;
import cn.td.aiot.common.domain.TiduConstant;
import cn.td.aiot.common.utils.SortUtil;
import cn.td.aiot.location.dao.BuildingMapper;
import cn.td.aiot.location.domain.Building;
import cn.td.aiot.location.service.BuildingService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * ClassName  BuildingServiceImpl <br/>
 * Description 建筑Service实现类 <br/>
 *
 * @author YeYouGui
 * @version 1.0
 * @date 2020/7/24 10:28<br/>
 * @since JDK 1.8
 */
@Service
public class BuildingServiceImpl extends ServiceImpl<BuildingMapper, Building> implements BuildingService {

    private final BuildingMapper buildingMapper;
    private String message;

    public BuildingServiceImpl(BuildingMapper buildingMapper) {
        this.buildingMapper = buildingMapper;
    }

    @Override
    public IPage<Building> findAllBuilding(QueryRequest queryRequest, Integer parkId) {
        try {
            Page<Building> page = new Page<>(queryRequest.getPageNum(), queryRequest.getPageSize());
            if (parkId != null) {
                QueryWrapper<Building> wrapper = new QueryWrapper<>();
                wrapper.eq("park_id", parkId);
                return buildingMapper.selectPage(page, wrapper);
            } else {
                SortUtil.handlePageSort(queryRequest, page, "build_id", TiduConstant.ORDER_DESC, false);
                return buildingMapper.selectPage(page, null);
            }
        } catch (Exception e) {
            message = "查询园区失败";
            log.error(message, e);
            return null;
        }

    }
}
