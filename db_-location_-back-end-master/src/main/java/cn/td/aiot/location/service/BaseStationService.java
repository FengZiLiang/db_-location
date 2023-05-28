package cn.td.aiot.location.service;

import cn.td.aiot.common.domain.QueryRequest;
import cn.td.aiot.location.domain.BaseStation;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * ClassName  BaseStationService <br/>
 * Description 定位基站服务类 <br/>
 *
 * @author YeYouGui
 * @version 1.0
 * @date 2020/7/24 10:23<br/>
 * @since JDK 1.8
 */
@DS("location")
public interface BaseStationService  extends IService<BaseStation> {

    /**
     * 分页查询所有基站信息
     * @param queryRequest 查询请求
     * @param mapId
     * @return IPage<BaseStation> </>
     */
    IPage<BaseStation> findAllBaseStation(QueryRequest queryRequest, String mapId);



}
