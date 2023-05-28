package cn.td.aiot.location.service;

import cn.td.aiot.common.domain.QueryRequest;
import cn.td.aiot.location.domain.Building;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * ClassName  BuildingService <br/>
 * Description 建筑基础Service <br/>
 *
 * @author YeYouGui
 * @version 1.0
 * @date 2020/7/24 10:24<br/>
 * @since JDK 1.8
 */
@DS("location")
public interface BuildingService extends IService<Building> {
    /**
     * 查询所有的园区
     *
     * @param parkId       如果为null则查询全部由
     * @param queryRequest 分页查询
     * @return IPage<Building>
     */
    IPage<Building> findAllBuilding(QueryRequest queryRequest, Integer parkId);

}
