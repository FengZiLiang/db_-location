package cn.td.aiot.decoder.utils.commandManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * 改为读取yml的形式获取配置
 */
@Slf4j
@Scope("singleton")
@Component("MyFFmpegManager")
public  class TdFFmpegManagerImpl extends CommandManagerImpl {
    private static TdFFmpegManagerImpl INSTANC;

    public static void setINSTANC(TdFFmpegManagerImpl INSTANC) {
        TdFFmpegManagerImpl.INSTANC = INSTANC;
    }

    public static synchronized TdFFmpegManagerImpl getInstance() {
        if (INSTANC == null){
            INSTANC = new TdFFmpegManagerImpl();
        }
        return INSTANC;
    }


}
