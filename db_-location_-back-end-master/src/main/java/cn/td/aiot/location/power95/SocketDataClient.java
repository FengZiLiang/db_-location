package cn.td.aiot.location.power95;


import cn.td.aiot.common.domain.TiduConstant;
import cn.td.aiot.common.exception.RedisConnectException;
import cn.td.aiot.common.service.RedisService;
import cn.td.aiot.common.utils.ByteChangeUtil;
import cn.td.aiot.location.domain.*;
import cn.td.aiot.location.factory.EnclosureActionFactory;
import cn.td.aiot.location.service.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class SocketDataClient {

    private final RedisService redisService;
    private final TerminalService terminalService;
    private final BaseStationService baseStationService;
    private final LocationService locationService;
    private final Gson gson;

    private final static long THRESHOLD = 500L;
    @Autowired
    EnclosureService enclosureService;
    @Autowired
    EnclosureRuleService enclosureRuleService;

    public SocketDataClient(RedisService redisService, TerminalService terminalService,
                            BaseStationService baseStationService, LocationService locationService, Gson gson) {
        this.redisService = redisService;
        this.terminalService = terminalService;
        this.baseStationService = baseStationService;
        this.locationService = locationService;
        this.gson = gson;
    }

    /**
     * 获取实时定位数据
     *
     * @param serverName 服务器ip
     * @param port       服务器端口
     */
    public void doWork(String serverName, Integer port) {
        try {
            SocketChannel client = SocketChannel.open(new InetSocketAddress(serverName, port));
            while (true) {
                ByteBuffer byteBuffer = ByteBuffer.allocateDirect(256);
                // 关闭IO阻塞
                client.configureBlocking(false);
                // 如果成功连接
                if (client.isConnected()) {
                    client.write(byteBuffer);
                    byteBuffer.clear();
                    if (client.read(byteBuffer) > 0) {
                        byteBuffer.flip();
                        byte[] buff = new byte[byteBuffer.remaining()];
                        byteBuffer.get(buff);
                        String df1 = "yyyy-MM-dd HH:mm:ss.SSS";
                        Long time = System.currentTimeMillis();
                        String timeStamp = new SimpleDateFormat(df1).format(time);
                        if ("65".equalsIgnoreCase(Long.toHexString(buff[0]).toUpperCase())) {
                            byte[] buffX = {buff[1], buff[2], buff[3], buff[4], buff[5], buff[6], buff[7], buff[8]};
                            byte[] buffY = {buff[9], buff[10], buff[11], buff[12], buff[13], buff[14], buff[15], buff[16]};
                            byte[] buffZ = {buff[17], buff[18], buff[19], buff[20], buff[21], buff[22], buff[23], buff[24]};
                            byte[] buffMac = {buff[25], buff[26], buff[27], buff[28], buff[29], buff[30], buff[31], buff[32]};
                            double locationX = Double.parseDouble(String.format("%.2f", ByteChangeUtil.bytes2Double(buffX)));
                            double locationY = Double.parseDouble(String.format("%.2f", ByteChangeUtil.bytes2Double(buffY)));
                            double locationZ = Double.parseDouble(String.format("%.2f", ByteChangeUtil.bytes2Double(buffZ)));
                            String deviceMac = ByteChangeUtil.byteToHex(buffMac);
                            //通过基站查询用户Id和地图Id TODO 基站应该通过指令获取
                            Map<String, Object> userIdAndMapId = findUserIdAndMapId(deviceMac, "08AA591DDA06");
                            Integer userId = (Integer) userIdAndMapId.get("userId");
                            Integer mapId = (Integer) userIdAndMapId.get("mapId");
                            // 将数据封装到对象
                            Location location = new Location();
                            location.setUserId(userId);
                            location.setLocationX(locationX);
                            location.setLocationY(locationY);
                            location.setLocationZ(locationZ);
                            location.setMapId(mapId);
                            location.setLocationTime(Timestamp.valueOf(timeStamp));
                            location.setMac(deviceMac);


                            //存储实时数据到redis
                            redisService.hset(TiduConstant.LOCATION_REALTIME_REDIS, deviceMac, gson.toJson(location), 60000L);
//                            redisService.set(TiduConstant.LOCATION_REALTIME_REDIS, gson.toJson(location), 60000L);

                            //缓存历史数据,时间为精度为秒
                            String df2 = "yyyy-MM-dd HH:mm:ss";
                            String time2 = new SimpleDateFormat(df2).format(time);
                            location.setLocationTime(Timestamp.valueOf(time2));
                            cacheHistoryLocation(location);
                        }
                    }
                }
            }
        } catch (Exception e) {
            reconnect(serverName, port);
        }

    }

    // 这是模拟数据的
    public void doWork(String servername, Integer port, boolean isReal) {
        try {
            while (true) {
                String df1 = "yyyy-MM-dd HH:mm:ss.SSS";
                Long time = System.currentTimeMillis();
                String timeStamp = new SimpleDateFormat(df1).format(time);
                double locationX = Double.parseDouble(String.format("%.2f", (Math.random() * 10) / 2));
                double locationY = Double.parseDouble(String.format("%.2f", (Math.random() * 10) / 2));
                double locationZ = Double.parseDouble(String.format("%.2f", (Math.random() * 10) / 2));
                String deviceMac = "2C4181CF0001CADE";
                //通过基站查询用户Id和地图Id TODO 基站应该通过指令获取
                Map<String, Object> userIdAndMapId = findUserIdAndMapId(deviceMac, "08AA591DDA06");
                Integer userId = (Integer) userIdAndMapId.get("userId");
                Integer mapId = (Integer) userIdAndMapId.get("mapId");
                // 将数据封装到对象
                Location location = new Location();
                location.setUserId(userId);
                location.setLocationX(locationX);
                location.setLocationY(locationY);
                location.setLocationZ(locationZ);
                location.setMapId(mapId);
                location.setLocationTime(Timestamp.valueOf(timeStamp));
                location.setMac(deviceMac);


                //存储实时数据到redis
                redisService.hset(TiduConstant.LOCATION_REALTIME_REDIS, deviceMac, gson.toJson(location), 60000L);
//                            redisService.set(TiduConstant.LOCATION_REALTIME_REDIS, gson.toJson(location), 60000L);

                //缓存历史数据,时间为精度为秒
                String df2 = "yyyy-MM-dd HH:mm:ss";
                String time2 = new SimpleDateFormat(df2).format(time);
                location.setLocationTime(Timestamp.valueOf(time2));
                cacheHistoryLocation(location);

            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * 电子围栏行为检测
     *
     * @param location 终端的位置
     */
    @Async(value = "taskExecutor")
    public void behaviorDetection(Location location) {
        //查询围栏的位置
        QueryWrapper<Enclosure> enclosureQueryWrapper = new QueryWrapper<>();
        enclosureQueryWrapper.select("id", "enclosure_location", "enclosure_name");
        List<Enclosure> list = enclosureService.list(enclosureQueryWrapper);
        if (!list.isEmpty()) {
            list.forEach(enclosure -> {
                Integer enclosureId = enclosure.getId();
                QueryWrapper<EnclosureRule> ruleQueryWrapper = new QueryWrapper<>();
                ruleQueryWrapper.eq("enclosure_id", enclosureId);
                ruleQueryWrapper.eq("list_type", 1);
                EnclosureRule rule = enclosureRuleService.getOne(ruleQueryWrapper);
                if (rule != null) {
                    String forPeopleStr = rule.getForPeople();
                    Boolean isForever = rule.getIsForever();
                    String expireTimeStr = rule.getExpireTime();
                    String characterType = rule.getCharacterType();
                    // 黑名单列表,Map的key为name、userId
                    List<Map<String, Object>> peopleList = gson.fromJson(forPeopleStr, new TypeToken<List<Map<String, Object>>>() {
                    }.getType());
                    // 有效时间,Map的key为startTime,endTime
                    List<Map<String, Object>> expireTime = gson.fromJson(expireTimeStr, new TypeToken<List<Map<String, Object>>>() {
                    }.getType());

                    // 查询电子围栏位置
                    String enclosureLocation = enclosure.getEnclosureLocation();
                    List<Map<String, Object>> enclosureLocations = gson.fromJson(enclosureLocation, new TypeToken<List<Map<String, Object>>>() {
                    }.getType());
                    //判断规则是否永久有效
                    if (isForever) {
                        saveActionToRedis(location, characterType, peopleList, enclosureLocations);
                    } else {
                        //如果不是永久有效,则判断时间是否处于有效时间,最多可设置两个时间段
                        Map<String, Object> expireTime1 = expireTime.get(0);
                        int startTime1 = (int) expireTime1.get("startTime");
                        int endTime1 = (int) expireTime1.get("endTime");
                        Map<String, Object> expireTime2 = expireTime.get(1);
                        int startTime2 = (int) expireTime2.get("startTime");
                        int endTime2 = (int) expireTime2.get("endTime");
                        //获取系统时间，并格式化为HH
                        String df1 = "HH";
                        Long time = System.currentTimeMillis();
                        int hour = Integer.parseInt(new SimpleDateFormat(df1).format(time));
                        if ((startTime1 <= hour && hour <= endTime1) || (startTime2 <= hour && hour <= endTime2)) {
                            saveActionToRedis(location, characterType, peopleList, enclosureLocations);
                        }
                    }
                }

            });
        }

    }


    /**
     * 重连方法
     *
     * @param serverName 服务器IP
     * @param port       服务器端口
     */
    public void reconnect(String serverName, Integer port) {
        try {
            Thread.sleep(5000);
            doWork(serverName, port, false);
            System.out.println("正在重连");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    /**
     * 向 redis缓存历史数据
     *
     * @param location 数据
     */

    public void cacheHistoryLocation(Location location) {
        try {
            // 获取长度
            long len = redisService.hlen(TiduConstant.LOCATION_HISTORY_REDIS);
            // 如果长度到达指定数量
            if (len >= THRESHOLD) {
                // 查询所有的数据
                List<String> locations = redisService.hvals(TiduConstant.LOCATION_HISTORY_REDIS);
                enclosureRedisToMysql(locations);
            }
            String key = location.getUserId() + "|" + location.getMapId() + "|" + location.getLocationTime().getTime();
            redisService.hset(TiduConstant.LOCATION_HISTORY_REDIS, key, gson.toJson(location));
            Thread.sleep(1000L);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 将redis的缓存持久化到mysql
     */
    public void enclosureRedisToMysql(List<String> locations) {
        //转为对像
        List<Location> locationList = new ArrayList<>();
        try {
            locations.forEach(lcstr -> {
                Location location = gson.fromJson(lcstr, Location.class);
                locationList.add(location);
            });

            boolean isSuccess = locationService.saveBatch(locationList);
            if (isSuccess) {
                //删除key
                redisService.del(TiduConstant.LOCATION_HISTORY_REDIS);
            }
        } catch (Exception e) {
            /*
             由于批量插入会导致主键异常，故需要删除对应的行数在插入
            */
            String exception = e.toString();
            int index = exception.lastIndexOf("Duplicate");
            String key = exception.substring(index);
            String s1 = key.split(" ")[2] + " " + key.split(" ")[3];
            int index1 = s1.indexOf('-');
            s1 = s1.replace("\'", "");
            String uid = s1.substring(0, index1 - 1);
            String date = s1.substring(index1);
            Map<String, Object> map = new HashMap<>(12);
            map.put("user_id", uid);
            map.put("location_time", date);
            boolean del = locationService.removeByMap(map);
            if (del) {
                log.error("删除重复主键成功");
            } else {
                log.error("添加数据库失败", e);
            }


        }
    }

    /**
     * 通过基站mac和设备mac查询用户Id和 地图
     *
     * @param deviceMac 设备mac
     * @param baseMac   基站mac
     * @return Map
     */
    private Map<String, Object> findUserIdAndMapId(String deviceMac, String baseMac) {
        if (baseMac != null) {
            Map<String, Object> map = new HashMap<>(12);
            Terminal terminal = terminalService.getById(deviceMac);
            Integer userId = terminal.getUserId();

            BaseStation baseStation = baseStationService.getById(baseMac);
            Integer mapId = baseStation.getMapId();
            map.put("userId", userId);
            map.put("mapId", mapId);
            return map;
        }
        return null;
    }


    /**
     * 保存规则到redis
     *
     * @param location           位置
     * @param characterType      规则类型
     * @param peopleList         人员名单
     * @param enclosureLocations 电子围栏位置
     */
    private void saveActionToRedis(Location location, String characterType, List<Map<String, Object>> peopleList, List<Map<String, Object>> enclosureLocations) {
        try {
            for (Map<String, Object> map : peopleList) {
                Integer userId = (Integer) map.get("userId");
                //人员是否存在黑名单中
                if (userId.equals(location.getUserId())) {
                    //进行规则检测
                    EnclosureActionFactory factory = new EnclosureActionFactory();
                    Warning actionVo = factory.getActionHandler(characterType).enterAction(location, enclosureLocations);
                    String actionStr = gson.toJson(actionVo);
                    //存储到redis
                    redisService.rpush(TiduConstant.LOCATION_ENCLOSURE_WARNING, actionStr);
                }
            }
        } catch (RedisConnectException e) {
            throw new RuntimeException(e);
        }
    }


    private void saveActionToMySQL(Warning warning) {
        try {
            Long llen = redisService.llen(TiduConstant.LOCATION_ENCLOSURE_WARNING);
            if (llen >= THRESHOLD) {
                //TODO 待转存到数据库
            }

        } catch (RedisConnectException e) {
            e.printStackTrace();
        }
    }
}

