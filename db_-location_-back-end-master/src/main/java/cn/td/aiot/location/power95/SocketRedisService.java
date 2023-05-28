package cn.td.aiot.location.power95;

import cn.td.aiot.common.exception.RedisConnectException;
import cn.td.aiot.common.service.RedisService;
import cn.td.aiot.location.domain.Location;
import com.google.gson.Gson;
import org.springframework.stereotype.Service;

/**
 * SocketRedis服务层
 *
 * @author YeYouGui
 */
@Service
public class SocketRedisService {
    private final RedisService redisService;
    private final Gson gson;

    public SocketRedisService(RedisService redisService, Gson gson) {
        this.redisService = redisService;
        this.gson = gson;
    }

    /**
     * 添加定位数据
     *
     * @param key      redis 键
     * @param location 位置
     */
    public void addLocation(String key, Location location) {
        try {
            redisService.set("location_" + key, gson.toJson(location), 60000L);
        } catch (RedisConnectException e) {
            e.printStackTrace();
        }
    }

    /**
     * 返回定位数据
     *
     * @param key redis 键
     * @return String
     */
    public String getLocation(String key) {
        try {
            return redisService.get("location_" + key);
        } catch (RedisConnectException e) {
            throw new RuntimeException(e);
        }
    }
}
