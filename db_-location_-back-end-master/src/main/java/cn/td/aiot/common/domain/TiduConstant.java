package cn.td.aiot.common.domain;

/**
 * FEBS常量
 */
public class TiduConstant {

    /**
     * user缓存前缀
     */
    public static final String USER_CACHE_PREFIX = "tidu.cache.user.";
    /**
     * user角色缓存前缀
     */
    public static final String USER_ROLE_CACHE_PREFIX = "tidu.cache.user.role.";
    /**
     * user权限缓存前缀
     */
    public static final String USER_PERMISSION_CACHE_PREFIX = "tidu.cache.user.permission.";
    /**
     * user个性化配置前缀
     */
    public static final String USER_CONFIG_CACHE_PREFIX = "tidu.cache.user.config.";
    /**
     * token缓存前缀
     */
    public static final String TOKEN_CACHE_PREFIX = "tidu.cache.token.";

    /**
     * 存储在线用户的 zset前缀
     */
    public static final String ACTIVE_USERS_ZSET_PREFIX = "tidu.user.active";

    /**
     * 验证码前缀
     */
    public static final String CODE_PREFIX = "tidu_captcha_";

    /**
     * 存储基站信息缓存前缀
     */
    public static final String LOCATION_BASE_PREFIX = "tidu.location.base.";


    /**
     * redis实时定位数据前缀
     */
    public static final String LOCATION_REALTIME_REDIS = "tidu.location.realtime";

    /**
     * redis历史轨迹缓存前缀
     */
    public static final String LOCATION_HISTORY_REDIS = "tidu.location.history.redis";

    /**
     * redis电子围栏报警
     */
    public static final String LOCATION_ENCLOSURE_WARNING = "tidu.location.enclosure.warning";


    /**
     * 排序规则： descend 降序
     */
    public static final String ORDER_DESC = "descend";
    /**
     * 排序规则： ascend 升序
     */
    public static final String ORDER_ASC = "ascend";

    /**
     * 按钮类型
     */
    public static final String TYPE_BUTTON = "1";

    /**
     * 菜单类型
     */
    public static final String TYPE_MENU = "0";


}
