package cn.td.aiot.location.service;

import cn.td.aiot.common.domain.QueryRequest;
import cn.td.aiot.location.domain.Floor;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * ClassName  FloorService <br/>
 * Description 楼层Service <br/>
 *
 * @author YeYouGui
 * @version 1.0
 * @date 2020/7/24 10:25<br/>
 * @since JDK 1.8
 */
@DS("location")
public interface FloorService extends IService<Floor> {

    /**
     * 查询所有的楼层
     *
     * @param request 分页查询
     * @param buildId 如果不为null则根据建筑Id查询，如果为null则查询全部
     * @return
     */
    IPage<Floor> findAllFloor(QueryRequest request, Integer buildId);

    /**
     * 根据楼层Id删除楼层和地图
     *
     * @param floorId 楼层id
     * @return int
     */
    int deleteFloorAndMap(Integer floorId);
}
