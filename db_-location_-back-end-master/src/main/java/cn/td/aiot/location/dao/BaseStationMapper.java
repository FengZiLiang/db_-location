package cn.td.aiot.location.dao;

import cn.td.aiot.location.domain.BaseStation;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * ClassName  BaseStationMapper <br/>
 * Description 基站Mapper <br/>
 *
 * @author YeYouGui
 * @version 1.0
 * @date 2020/7/24 10:17<br/>
 * @since JDK 1.8
 */
public interface BaseStationMapper extends BaseMapper<BaseStation> {
}
