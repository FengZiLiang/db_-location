package cn.td.aiot.common.service;

import cn.td.aiot.common.domain.RedisInfo;
import cn.td.aiot.common.exception.RedisConnectException;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface RedisService {


    /**
     * 获取 redis 的详细信息
     *
     * @return List
     */
    List<RedisInfo> getRedisInfo() throws RedisConnectException;

    /**
     * 获取 redis key 数量
     *
     * @return Map
     */
    Map<String, Object> getKeysSize() throws RedisConnectException;

    /**
     * 获取 redis 内存信息
     *
     * @return Map
     */
    Map<String, Object> getMemoryInfo() throws RedisConnectException;

    /**
     * 获取 key
     *
     * @param pattern 正则
     * @return Set
     */
    Set<String> getKeys(String pattern) throws RedisConnectException;

    /**
     * get命令
     *
     * @param key key
     * @return String
     */
    String get(String key) throws RedisConnectException;

    /**
     * set命令
     *
     * @param key   key
     * @param value value
     * @return String
     */
    String set(String key, String value) throws RedisConnectException;

    /**
     * set 命令
     *
     * @param key         key
     * @param value       value
     * @param milliscends 毫秒
     * @return String
     */
    String set(String key, String value, Long milliscends) throws RedisConnectException;


    /**
     * 获取所有的字段
     *
     * @param key key
     * @return hgetAll
     * @throws RedisConnectException
     */
    Map<String, String> hgetAll(String key) throws RedisConnectException;

    /**
     * haset 命令
     *
     * @param name  name
     * @param key   key
     * @param value value
     * @return Long
     */
    Long hset(String name, String key, String value) throws RedisConnectException;


    /**
     * haset 命令
     *
     * @param name   name
     * @param key    key
     * @param value  value
     * @param expire 有效时间
     * @return Long
     */
    Long hset(String name, String key, String value, Long expire) throws RedisConnectException;


    /**
     * hvals命令
     *
     * @param key key
     * @return List
     * @throws RedisConnectException
     */
    List<String> hvals(String key) throws RedisConnectException;

    /**
     * hlen命令 得到hash的总长
     *
     * @param key 键
     * @return Long
     */
    Long hlen(String key) throws RedisConnectException;

    /**
     * del命令
     *
     * @param key key
     * @return Long
     */
    Long del(String... key) throws RedisConnectException;

    /**
     * exists命令
     *
     * @param key key
     * @return Boolean
     */
    Boolean exists(String key) throws RedisConnectException;

    /**
     * pttl命令
     *
     * @param key key
     * @return Long
     */
    Long pttl(String key) throws RedisConnectException;

    /**
     * pexpire命令
     *
     * @param key         key
     * @param milliscends 毫秒
     * @return Long
     */
    Long pexpire(String key, Long milliscends) throws RedisConnectException;


    /**
     * zadd 命令
     *
     * @param key    key
     * @param score  score
     * @param member value
     */
    Long zadd(String key, Double score, String member) throws RedisConnectException;

    /**
     * zrangeByScore 命令
     *
     * @param key key
     * @param min min
     * @param max max
     * @return Set<String>
     */
    Set<String> zrangeByScore(String key, String min, String max) throws RedisConnectException;

    /**
     * zremrangeByScore 命令
     *
     * @param key   key
     * @param start start
     * @param end   end
     * @return Long
     */
    Long zremrangeByScore(String key, String start, String end) throws RedisConnectException;

    /**
     * zrem 命令
     *
     * @param key     key
     * @param members members
     * @return Long
     */
    Long zrem(String key, String... members) throws RedisConnectException;


    /**
     * rpus 命令
     *
     * @param key   key
     * @param value value
     * @return Long
     */
    Long rpush(String key, String... value) throws RedisConnectException;


    /**
     * lrange-获取列表指定的范围的元素
     *
     * @param key   键
     * @param start 开始
     * @param stop  结束
     * @return List
     */
    List<String> lrange(String key, Long start, Long stop) throws RedisConnectException;


    /**
     * 获取list长度
     *
     * @param key 键
     * @return long
     */
    Long llen(String key) throws RedisConnectException;

    /**
     * ltrim 保留指定范围的元素
     *
     * @param key        key
     * @param rangeBegin range_l
     * @param rangeEnd   range_r
     * @return String
     */
    String ltrim(String key, Long rangeBegin, Long rangeEnd) throws RedisConnectException;

    /**
     * 向list后面追加值
     *
     * @param key   键
     * @param value 值
     * @return Long
     */
    Long linsertAfter(String key, String lastRedisValue, String value) throws RedisConnectException;


    /**
     * 通过索引获取列表中的元素
     *
     * @param key   键值
     * @param index 索引
     */
    String lindexKey(String key, long index) throws RedisConnectException;
}
