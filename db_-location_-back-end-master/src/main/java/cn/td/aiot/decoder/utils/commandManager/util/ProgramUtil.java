package cn.td.aiot.decoder.utils.commandManager.util;

import cn.td.aiot.decoder.utils.commandManager.config.ProgramConfig;

public class ProgramUtil {
    //TODO: 有时间了仿照PropertiesUtil写一个获取SpringContext值的
    public static <T>T load(String path, Class<T> cl) {

        return (T)new ProgramConfig();
    }
}
