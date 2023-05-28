package cn.td.aiot.location.power95;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SocketRunner implements ApplicationRunner {//或者也可以实现这个接口CommandLineRunner
    @Autowired
    SocketDataClient dataClient;
    @Autowired
    SocketCommClient commClient;
    @Value("${tidu.location.serverName}")
    private String serverName;
    @Value("${tidu.location.serverCommPort}")
    private String serverCommPort;
    @Value("${tidu.location.serverDataPort}")
    private Integer serverDataPort;


    @Override
    public void run(ApplicationArguments args) throws Exception {
        //SocketClient client=new SocketClient();
        String file = "locationStart.xml";
        String startComm = XmlReadUtil.readXml(file);
        //先不要放开
        //commClient.doWork(startComm,serverName,serverDataPort);
        dataClient.doWork(serverName, serverDataPort, false);
    }
}
