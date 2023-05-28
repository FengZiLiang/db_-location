package cn.td.aiot.location.service.impl;

import cn.td.aiot.common.domain.TiduConstant;
import cn.td.aiot.common.exception.TdException;
import cn.td.aiot.common.service.RedisService;
import cn.td.aiot.location.dao.LocationMapper;
import cn.td.aiot.location.domain.Location;
import cn.td.aiot.location.service.LocationService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * ClassName  LocationServiceImpl <br/>
 * Description 位置信息Service实现 <br/>
 *
 * @author YeYouGui
 * @version 1.0
 * @date 2020/7/24 10:33<br/>
 * @since JDK 1.8
 */
@Service("location")
@Slf4j
public class LocationServiceImpl extends ServiceImpl<LocationMapper, Location> implements LocationService {
    private final LocationMapper locationMapper;
    private final RedisService redisService;
    private final Gson gson;

    public LocationServiceImpl(LocationMapper locationMapper, RedisService redisService, Gson gson) {
        this.locationMapper = locationMapper;
        this.redisService = redisService;
        this.gson = gson;
    }


    @Override
    public List<Location> findHistoryLocation(Integer userId, Integer mapId, String beginTime, String endTime) throws TdException {

        List<Location> toRedis = getToRedis(userId, mapId, beginTime, endTime);
        if (!toRedis.isEmpty()) {
            return toRedis;
        } else {
            QueryWrapper<Location> locationWrapper = new QueryWrapper<>();
            locationWrapper.select("location_x", "location_y", "location_z", "location_time");
            locationWrapper.eq("user_id", userId);
            locationWrapper.eq("map_id", mapId);
            locationWrapper.between("location_time", beginTime, endTime);
            locationWrapper.orderByAsc("location_time");
            return locationMapper.selectList(locationWrapper);
        }

    }


    /**
     * 查询redis数据库
     *
     * @param userId
     * @param mapId
     * @param beginTime
     * @param endTime
     * @return List<Location></>
     */
    private List<Location> getToRedis(Integer userId, Integer mapId, String beginTime, String endTime) throws TdException {
        String begin = String.valueOf(Timestamp.valueOf(beginTime).getTime());
        String end = String.valueOf(Timestamp.valueOf(endTime).getTime());

        List<Location> locations = new ArrayList<>();
        try {
            Map<String, String> locationMap = redisService.hgetAll(TiduConstant.LOCATION_HISTORY_REDIS);
            for (Map.Entry<String, String> map : locationMap.entrySet()) {

                String key = map.getKey();

                String[] keys = key.replace("|", ",").split(",");

                Integer uid = Integer.valueOf(keys[0]);
                Integer mId = Integer.valueOf(keys[1]);
                String timestamp = keys[2];
                // begin <= time <= end
                if (userId.equals(uid) && mId.equals(mapId) && timestamp.compareTo(begin) >= 0 && timestamp.compareTo(end) <= 0) {
                    String strlc = map.getValue();
                    Location location = gson.fromJson(strlc, Location.class);
                    locations.add(location);
                }

            }

            return locations.stream().sorted(Comparator.comparing(Location::getLocationTime)).collect(Collectors.toList());
        } catch (Exception e) {
            throw new TdException("查询失败");
        }

    }

}
