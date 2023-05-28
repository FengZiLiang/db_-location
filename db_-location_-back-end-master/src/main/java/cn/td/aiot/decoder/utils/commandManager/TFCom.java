package cn.td.aiot.decoder.utils.commandManager;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Slf4j
@Scope("singleton")
@Component
public class TFCom {
    @Value("${td.decoder.loadffmpeg.path}")
    String loadFFmpegPath;
    @Value("${td.decoder.loadffmpeg.size}")
    Integer loadFFmpegSize;
    @Value("${td.decoder.loadffmpeg.debug}")
    boolean loadFFmpegDebug;
    @Value("${td.decoder.loadffmpeg.keepalive}")
    boolean loadFFmpegKeepalive;

//    public final static FFmpegConfig config=new FFmpegConfig();



    @Bean
    public CommandManager myFFmpegManagerInit() {
        TdFFmpegManagerImpl.config.setPath(loadFFmpegPath);
        TdFFmpegManagerImpl.config.setDebug(loadFFmpegDebug);
        TdFFmpegManagerImpl.config.setSize(loadFFmpegSize);
        TdFFmpegManagerImpl.config.setKeepalive(loadFFmpegKeepalive);
        log.info(TdFFmpegManagerImpl.config.toString());
        return TdFFmpegManagerImpl.getInstance();
    }

    @PreDestroy
    public void myFFmpegManagerPre() {
        //服务关闭时关闭所有推流

        TdFFmpegManagerImpl.getInstance().stopAll();
    }


}
