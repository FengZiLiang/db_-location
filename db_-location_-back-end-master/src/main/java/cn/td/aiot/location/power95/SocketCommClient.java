package cn.td.aiot.location.power95;


import cn.td.aiot.location.dao.EnclosureMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.Socket;

@Service
public class SocketCommClient {

    @Value("${tidu.location.serverName}")
    private String serverName;
    @Value("${tidu.location.serverCommPort}")
    private Integer serverCommPort;


    public void doWork(String startComm) {
        try {
            Socket s = new Socket(serverName, serverCommPort);

            //构建IO
            InputStream is = s.getInputStream();
            OutputStream os = s.getOutputStream();
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os));
            //向服务器端发送一条消息
            bw.write(startComm);
            bw.flush();

            //读取服务器返回的消息
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String mess = br.readLine();
            System.out.println("服务器：" + mess);
            s.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

