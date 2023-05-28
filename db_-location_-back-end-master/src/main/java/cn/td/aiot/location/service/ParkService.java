package cn.td.aiot.location.service;

import cn.td.aiot.common.domain.QueryRequest;
import cn.td.aiot.location.domain.Park;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * ClassName  ParkService <br/>
 * Description 园区Service <br/>
 *
 * @author YeYouGui
 * @version 1.0
 * @date 2020/7/24 10:26<br/>
 * @since JDK 1.8
 */
@DS("location")
public interface ParkService extends IService<Park> {
    /**
     * 查询所有的园区
     *
     * @param queryRequest 分页请求
     * @return Ipage<Park></>
     */
    IPage<Park> findAllPark(QueryRequest queryRequest);
}
