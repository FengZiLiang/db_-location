package cn.td.aiot.location.service;

import cn.td.aiot.common.exception.TdException;
import cn.td.aiot.location.domain.Location;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * ClassName  LocationService <br/>
 * Description 位置Service <br/>
 *
 * @author YeYouGui
 * @version 1.0
 * @date 2020/7/24 10:25<br/>
 * @since JDK 1.8
 */
@DS("location")
public interface LocationService extends IService<Location> {
    List<Location> findHistoryLocation(Integer userId, Integer mapId, String beginTime, String endTime) throws TdException;

}
