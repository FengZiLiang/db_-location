package cn.td.aiot.location.service.impl;

import cn.td.aiot.common.domain.QueryRequest;
import cn.td.aiot.common.utils.SortUtil;
import cn.td.aiot.location.dao.BaseStationMapper;
import cn.td.aiot.location.domain.BaseStation;
import cn.td.aiot.location.service.BaseStationService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * ClassName  BaseStationServiceImpl <br/>
 * Description 定位基站Service实现 <br/>
 *
 * @author YeYouGui
 * @version 1.0
 * @date 2020/7/24 10:28<br/>
 * @since JDK 1.8
 */
@Slf4j
@Service("BaseStation")
public class BaseStationServiceImpl extends ServiceImpl<BaseStationMapper, BaseStation> implements BaseStationService {
    private final BaseStationMapper baseStationMapper;

    public BaseStationServiceImpl(BaseStationMapper baseStationMapper) {
        this.baseStationMapper = baseStationMapper;
    }


    @Override
    public IPage<BaseStation> findAllBaseStation(QueryRequest request, String mapId) {
        try {
            Page<BaseStation> page = new Page<>(request.getPageNum(), request.getPageSize());
            if (mapId != null) {
                // 通过地图Id查询
                QueryWrapper<BaseStation> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("map_id", mapId);
                return baseStationMapper.selectPage(page, queryWrapper);
            } else {
                // 查询所有的基站
                SortUtil.handlePageSort(request, page);
                return baseStationMapper.selectPage(page, null);
            }

        } catch (Exception e) {
            log.error("查询基站异常", e);
            return null;
        }
    }
}
