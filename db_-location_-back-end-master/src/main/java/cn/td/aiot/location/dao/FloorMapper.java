package cn.td.aiot.location.dao;

import cn.td.aiot.location.domain.Floor;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

/**
 * ClassName  FloorMapper <br/>
 * Description  楼层基础Mapper<br/>
 *
 * @author YeYouGui
 * @version 1.0
 * @date 2020/7/24 10:19<br/>
 * @since JDK 1.8
 */
public interface FloorMapper extends BaseMapper<Floor> {

    /**
     * 根据楼层Id删除楼层和地图
     *
     * @param floorId 楼层id
     * @return int
     */
    int deleteFloorAndMap(Integer floorId);
}
