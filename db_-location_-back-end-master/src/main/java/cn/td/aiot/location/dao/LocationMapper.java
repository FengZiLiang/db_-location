package cn.td.aiot.location.dao;

import cn.td.aiot.location.domain.Location;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

/**
 * ClassName  LocationMapper <br/>
 * Description 位置基础Mapper <br/>
 *
 * @author YeYouGui
 * @version 1.0
 * @date 2020/7/24 10:20<br/>
 * @since JDK 1.8
 */
public interface LocationMapper extends BaseMapper<Location> {
}
