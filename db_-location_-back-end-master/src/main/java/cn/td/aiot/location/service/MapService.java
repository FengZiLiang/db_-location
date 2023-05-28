package cn.td.aiot.location.service;

import cn.td.aiot.common.domain.QueryRequest;
import cn.td.aiot.location.domain.LocationMap;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * ClassName  MapService <br/>
 * Description 地图Service <br/>
 *
 * @author YeYouGui
 * @version 1.0
 * @date 2020/7/24 10:26<br/>
 * @since JDK 1.8
 */
@DS("location")
public interface MapService extends IService<LocationMap> {

    /**
     * 查询所有的地图
     * @param request request
     * @return
     */
    IPage<LocationMap> findAllMap (QueryRequest request);
}
