package cn.td.aiot.location.service.impl;

import cn.td.aiot.common.domain.QueryRequest;
import cn.td.aiot.common.domain.TiduConstant;
import cn.td.aiot.common.utils.SortUtil;
import cn.td.aiot.location.dao.FloorMapper;
import cn.td.aiot.location.domain.Floor;
import cn.td.aiot.location.service.FloorService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * ClassName  FloorServiceImpl <br/>
 * Description 楼层Service实现 <br/>
 *
 * @author YeYouGui
 * @version 1.0
 * @date 2020/7/24 10:32<br/>
 * @since JDK 1.8
 */
@Service
public class FloorServiceImpl extends ServiceImpl<FloorMapper, Floor> implements FloorService {
    private final FloorMapper floorMapper;
    private String message;

    public FloorServiceImpl(FloorMapper floorMapper) {
        this.floorMapper = floorMapper;
    }


    @Override
    public IPage<Floor> findAllFloor(QueryRequest request, Integer buildId) {
        try {
            Page<Floor> floorPage = new Page<>(request.getPageNum(), request.getPageSize());
            if (buildId != null) {
                QueryWrapper<Floor> floorQueryWrapper = new QueryWrapper<>();
                floorQueryWrapper.eq("build_id", buildId);
                return floorMapper.selectPage(floorPage, floorQueryWrapper);
            } else {
                SortUtil.handlePageSort(request, floorPage, "floor_id", TiduConstant.ORDER_DESC, false);
                return floorMapper.selectPage(floorPage, null);
            }
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 通过楼层Id删除楼层和地图
     *
     * @param floorId 楼层id
     * @return int
     */
    @Override
    public int deleteFloorAndMap(Integer floorId) {
        return floorMapper.deleteFloorAndMap(floorId);
    }
}
