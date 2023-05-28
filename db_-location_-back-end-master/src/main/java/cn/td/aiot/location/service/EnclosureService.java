package cn.td.aiot.location.service;

import cn.td.aiot.common.domain.QueryRequest;
import cn.td.aiot.location.domain.Enclosure;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * ClassName  EnclosureService <br/>
 * Description  电子围栏服务层<br/>
 *
 * @author YeYouGui
 * @version 1.0
 * @date 2020/8/5 14:32<br/>
 * @since JDK 1.8
 */
@DS("location")
public interface EnclosureService extends IService<Enclosure> {

    /**
     * 分页查询所有的电子围栏
     *
     * @param request 分页查询的request
     * @return IPage
     */
    IPage<Enclosure> findAllEnclosure(QueryRequest request);
}
