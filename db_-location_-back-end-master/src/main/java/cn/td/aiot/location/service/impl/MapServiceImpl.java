package cn.td.aiot.location.service.impl;

import cn.td.aiot.common.domain.QueryRequest;
import cn.td.aiot.common.domain.TiduConstant;
import cn.td.aiot.common.utils.SortUtil;
import cn.td.aiot.location.dao.MapMapper;
import cn.td.aiot.location.domain.LocationMap;
import cn.td.aiot.location.service.MapService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * ClassName  MapServiceImpl <br/>
 * Description 地图Service实现  <br/>
 *
 * @author YeYouGui
 * @version 1.0
 * @date 2020/7/24 10:36<br/>
 * @since JDK 1.8
 */
@Slf4j
@Service
public class MapServiceImpl extends ServiceImpl<MapMapper, LocationMap> implements MapService {
    private final MapMapper mapMapper;

    public MapServiceImpl(MapMapper mapMapper) {
        this.mapMapper = mapMapper;
    }


    @Override
    public IPage<LocationMap> findAllMap(QueryRequest request) {
        try {
            Page<LocationMap> mapPage = new Page<>(request.getPageNum(), request.getPageSize());
            SortUtil.handlePageSort(request, mapPage, "map_id", TiduConstant.ORDER_DESC, false);
            return mapMapper.selectPage(mapPage, null);
        } catch (Exception e) {
            log.error("查询基站异常", e);
            return null;
        }

    }
}
