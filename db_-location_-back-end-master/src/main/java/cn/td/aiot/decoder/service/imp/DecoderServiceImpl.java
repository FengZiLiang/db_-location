package cn.td.aiot.decoder.service.imp;

import cn.td.aiot.common.exception.TdException;
import cn.td.aiot.decoder.controller.UnknownController;
import cn.td.aiot.decoder.dao.EtbaseCameraMapper;
import cn.td.aiot.decoder.domain.EtbaseCamera;
import cn.td.aiot.decoder.domain.ProbeEntity;
import cn.td.aiot.decoder.utils.commandManager.CommandManager;
import cn.td.aiot.decoder.utils.commandManager.TdFFmpegManagerImpl;
import cn.td.aiot.decoder.utils.commandManager.data.CommandTasker;
import cn.td.aiot.decoder.vo.DecoderVo;
import cn.td.aiot.decoder.service.DecoderService;
import cn.td.aiot.decoder.utils.ProcessUtils;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.*;
import java.util.*;
import java.util.concurrent.TimeoutException;

/**
 * 解码器service实现类.
 * @author :  fangguitang
 * @version :1.0
 */
@Slf4j
@Service
public class DecoderServiceImpl implements DecoderService {
    @Resource(name = "taskExecutor")
    ThreadPoolTaskExecutor taskExecutor;
    @Autowired
    EtbaseCameraMapper etbaseCameraMapper;
    @Autowired
    UnknownController unknownController;

/*    @Value("${td.decoder.loadffmpeg.path}")
    static String loadFFmpegPath;
    @Value("${td.decoder.loadffmpeg.size}")
    static Integer loadFFmpegSize;
    @Value("${td.decoder.loadffmpeg.debug}")
    static boolean loadFFmpegDebug;*/

    @Override
    public boolean canConnnect(String id,String rtspStr) throws IOException, InterruptedException, TdException, TimeoutException {
        getInstance();
        //TODO 可以考虑指定目录
        String command =  CommandManager.config.getPath()+ "ffmpeg -i " + rtspStr + " -f image2 -an -r 1 -t 00:00:01 -y test.jpg";
        log.info("测试rtsp连接command：" + command);
        Process process = ProcessUtils.executeCommand(command, 8000);
        //取得命令结果的输出流
        InputStream fis = process.getErrorStream();
        //用一个读输出流类去读
        InputStreamReader isr = new InputStreamReader(fis);
        //用缓冲器读行
        BufferedReader br = new BufferedReader(isr);
        String line;
        //直到读完为止
        boolean flag = true;
        while ((line = br.readLine()) != null) {
            if (line.contains("401 Unauthorized")) {
                throw new TdException("认证错误,请检查用户名密码");
            }
            if (line.contains("failed")){
                throw new TdException(line.getBytes().toString());
            }
            if (line.contains("Unknown error")){
                throw new TdException(line.getBytes().toString());
            }
            System.out.println(line);
        }
        return true;
    }
    /**
     * 获取Ffmpeg Manager(单例)
     */
/*    @Autowired
    @Qualifier("MyFFmpegManager")*/
//    private MyFFmpegManager manager;
//    @Bean
    private  synchronized CommandManager getInstance() {
/*        if (manager == null) {
            manager = new MyFFmpegManagerImpl();

        }*/
        return TdFFmpegManagerImpl.getInstance();
    }


    @Override
    public String startTranscoding(DecoderVo decoderVo) throws InterruptedException, TimeoutException, TdException, IOException {
        EtbaseCamera etbaseCamera = null;
        //任务ID
        String  taskId= String.valueOf(decoderVo.getSrcId());


        CommandManager manager = getInstance();
        CommandTasker commandTasker = manager.query(taskId);
        if (commandTasker != null){
            if (commandTasker.getThread().isAlive() && (System.currentTimeMillis() - commandTasker.getThread().getOutTime() < 10000)){
                    //存在任务则返回任务id
                    return commandTasker.getId();
            }else {
                //否则停止
                manager.stop(taskId);
            }
        }
        try{
            etbaseCamera = etbaseCameraMapper.selectById(decoderVo.getSrcId());
            if (etbaseCamera==null){
                throw new TdException("ID错误");
            }

        }catch (Exception e){
            throw new TdException("ID错误");
        }


        String ip = etbaseCamera.getIp();
        // 查询全部
        log.info("查询全部");
        Collection<CommandTasker> infoList = manager.queryAll();
        System.out.println(infoList);
        //TODO: 之后做多协议转换这之后的都要改，现在先写死

        // 解决ip输入错误时，grabber.start();出现阻塞无法释放grabber而导致后续推流无法进行；
        Socket rtspSocket = new Socket();
        Socket rtmpSocket = new Socket();
        // 建立TCP Scoket连接，超时时间1s，如果成功继续执行，否则return
        try {
            rtspSocket.connect(new InetSocketAddress(ip,etbaseCamera.getPort()),1000);
            rtspSocket.close();
//            rtspSocket.connect(new InetSocketAddress(cameraPojo.getIp(), 554), 1000);
        } catch (IOException e) {
            List<ProbeEntity> probeEntityList = unknownController.getAllUnknownDevices();
            //TODO 现在先后端执行更新IP，到时后前端弄
            for (ProbeEntity probeEntity : probeEntityList){
//                log.warn(probeEntity.getMac());
//                log.warn(etbaseCamera.getMac());
//                log.warn(String.valueOf(probeEntity.getMac().equals(etbaseCamera.getMac())));
                if (probeEntity.getMac().equals(etbaseCamera.getMac())){
                    etbaseCamera.setIp(probeEntity.getIpv4Address());
                    etbaseCameraMapper.updateById(etbaseCamera);
//                    log.warn("更新MAC为：" + etbaseCamera.getMac() + ",需更新的IP：" + ip + ",新的IP：" + etbaseCamera.getIp());
                    ip = etbaseCamera.getIp();
                    break;
                }
            }
            try {

//                log.warn(ip);
                rtspSocket = new Socket();
                rtspSocket.connect(new InetSocketAddress(ip,etbaseCamera.getPort()),1000);
                rtspSocket.close();
            }catch (Exception e1){
                throw new TdException("与拉流地址建立连接失败...");
            }
        }

        try {
            rtmpSocket.connect(new InetSocketAddress("127.0.0.1",1935), 1000);
/*            rtmpSocket.connect(new InetSocketAddress(IpUtil.IpConvert(config.getPush_ip()),
                    Integer.parseInt(config.getPush_port())), 1000);*/
            rtspSocket.close();
        } catch (IOException e) {
            rtspSocket.close();
            throw new TdException("与推流地址建立连接失败...");
        }
        //连接测试
 /*       try {
            canConnnect(String.valueOf(decoderVo.getSrcId()),url);
        }catch (Exception e){
            List<ProbeEntity> probeEntityList = unknownController.getAllUnknownDevices();
            //TODO 现在先后端执行更新IP，到时后前端弄
            for (ProbeEntity probeEntity : probeEntityList){
                if (probeEntity.getMac().equals(etbaseCamera.getMac())){
                    etbaseCamera.setIp(probeEntity.getIpv4Address());
                    etbaseCameraMapper.updateById(etbaseCamera);
                }
            }
            try {
                ip = etbaseCamera.getIp();
                url = "rtsp://" + etbaseCamera.getUserName() + ":" + etbaseCamera.getPassword() + "@" + ip + ":"+etbaseCamera.getPort();
                canConnnect(String.valueOf(decoderVo.getSrcId()),url);
            }catch (TdException tde){
//                throw tde;
            }catch (Exception e1){
//                throw new TdException("无法连接");

            }
        }*/
        String url = "rtsp://" + etbaseCamera.getUserName() + ":" + etbaseCamera.getPassword() + "@" + ip + ":" + etbaseCamera.getPort();

        Map<String,String> map = new HashMap<>();
        /*map.put("appName", "hello");*/
        //用ip当做id
//        map.put("appName", Base62Util.fromBase10(decoderVo.getSrcId())+Base62Util.fromBase10(System.currentTimeMillis()));
        map.put("appName", String.valueOf(decoderVo.getSrcId()));
        map.put("input", url);
        map.put("output", "rtmp://localhost/live/");
        //TODO:以后需要添加代码段验证
        map.putAll(decoderVo.getOutOptions());
/*        map.put("codec", "h264");
        map.put("fmt", "flv");
        map.put("fps", "25");
        map.put("rs", "640x360");
        map.put("twoPart", "1");*/
        String id="";
        try {
            log.info("开始执行");
//            id = manager.start(map);
            id = manager.start(String.valueOf(decoderVo.getSrcId()),"ffmpeg -i "+url+" -vcodec libx264 -vprofile baseline -acodec aac -strict -2 -s "+decoderVo.getOutOptions().get("rs")+/*" -q 10 " +*/ " -f flv "+map.get("output")+ taskId);
            log.info(String.valueOf(manager.query(id)));
            manager.query(id).getProcess();
        }catch (Exception e){
            manager.stop(id);
            id="";
        }
        // 执行任务，id就是appName，如果执行失败返回为null
        return id;
    }


    @Override
    public boolean stopTranscoding() {
        CommandManager manager = getInstance();
        Collection<CommandTasker> taskEntities = manager.queryAll();
        int res = manager.stopAll();
        log.info("停止解码，停止的个数为：" + res);
        return true;
    }


    /**
     * 组播地址
     */
    private final static String BC_IP = "239.255.255.250";
    /**
     * 组播端口
     */
    private final static int BC_PORT = 37020;
    private final static int PACK_SIZE = 4096;
    private volatile boolean  keepListen = true;

    @Override
    public List<ProbeEntity> scanHKDevices() throws IOException, InterruptedException {
        MulticastSocket sock = new MulticastSocket(BC_PORT);
        InetAddress bcAddr = InetAddress.getByName(BC_IP);

        List<ProbeEntity> probeEntityList = new ArrayList<>();
        // 创建socket并加入组播地址
        sock.joinGroup(bcAddr);
        sock.setLoopbackMode(false); // 必须是false才能开启广播功能！！
        keepListen = true;

        Thread a = new Thread(() -> { // 接受广播消息的线程
            try {
                DatagramPacket inpack = new DatagramPacket(new byte[PACK_SIZE], PACK_SIZE);
                while (keepListen) {

                    sock.receive(inpack);
                    System.out.println("广播消息：" + new String(inpack.getData(), 0, inpack.getLength()));
                    //将xml信息转换为实体
                    ProbeEntity probe = conventStrToProbe(new String(inpack.getData(), 0, inpack.getLength()));
                    if (probe != null) {
                        if (!contains(probeEntityList, probe)) {
                            probeEntityList.add(probe);
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                if (sock != null) {
                    try {
                        sock.leaveGroup(bcAddr);
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
//                    sock.close();
                }
                //System.exit(1);
            } catch (DocumentException e) {
                e.printStackTrace();
            }
        });
        a.start();

        //最终关闭程序之前一定要关闭socket

        byte[] buf1 = "<?xml version=\"1.0\" encoding=\"utf-8\"?><Probe><Uuid>C50970A5-90DA-4911-A559-015BA4BEB6A3</Uuid><Types>inquiry</Types></Probe>".getBytes();
        DatagramPacket outpack = new DatagramPacket(buf1, buf1.length, bcAddr , BC_PORT);
        sock.send(outpack);
        //一秒的时间让子线程接收数据
        Thread.sleep(1000);
        keepListen = false;
        sock.send(outpack);
        sock.close();
        return probeEntityList;
    }

    @Override
    public boolean stopTranscoding(String id) {
        CommandManager manager = getInstance();
        Collection<CommandTasker> taskEntities = manager.queryAll();
        return manager.stop(id);
    }

    /**
     * 将xml信息转换为ProbeEntity
     * @param str str
     * @return 实体
     */
    private ProbeEntity conventStrToProbe(String str) throws DocumentException {
        ProbeEntity probe = new ProbeEntity();
        Document doc = DocumentHelper.parseText(str);
        Element root = doc.getRootElement();
        if (root.element("DeviceType") != null) {
            probe.setDeviceType(root.element("DeviceType").getStringValue());
        } else {
            //设备类型不存在,直接返回null
            return null;
        }
        if (root.element("DeviceDescription") != null) {
            probe.setDeviceDescription(root.element("DeviceDescription").getStringValue());
        }
        if (root.element("DeviceSN") != null) {
            probe.setDeviceSN(root.element("DeviceSN").getStringValue());
        }
        if (root.element("CommandPort") != null) {
            probe.setCommandPort(root.element("CommandPort").getStringValue());
        }
        if (root.element("HttpPort") != null) {
            probe.setHttpPort(root.element("HttpPort").getStringValue());
        }
        if (root.element("MAC") != null) {
            probe.setMac(root.element("MAC").getStringValue());
        }
        if (root.element("IPv4Address") != null) {
            probe.setIpv4Address(root.element("IPv4Address").getStringValue());
        }
        if (root.element("IPv4SubnetMask") != null) {
            probe.setIpv4SubnetMask(root.element("IPv4SubnetMask").getStringValue());
        }
        if (root.element("IPv4Gateway") != null) {
            probe.setIpv4Gateway(root.element("IPv4Gateway").getStringValue());
        }
        if (root.element("DHCP") != null) {
            probe.setDhcp(Boolean.parseBoolean(root.element("DHCP").getStringValue()));
        }
        if (root.element("AnalogChannelNum") != null) {
            probe.setAnalogChannelNum(Integer.parseInt(root.element("AnalogChannelNum").getStringValue()));
        }
        if (root.element("DigitalChannelNum") != null) {
            probe.setDigitalChannelNum(Integer.parseInt(root.element("DigitalChannelNum").getStringValue()));
        }
        if (root.element("SoftwareVersion") != null) {
            probe.setSoftwareVersion(root.element("SoftwareVersion").getStringValue());
        }
        if (root.element("DSPVersion") != null) {
            probe.setDspVersion(root.element("DSPVersion").getStringValue());
        }
        if (root.element("BootTime") != null) {
            probe.setBootTime(root.element("BootTime").getStringValue());
        }
        if (root.element("Encrypt") != null) {
            probe.setEncrypt(Boolean.parseBoolean(root.element("Encrypt").getStringValue()));
        }
        if (root.element("ResetAbility") != null) {
            probe.setResetAbility(Boolean.parseBoolean(root.element("ResetAbility").getStringValue()));
        }
        if (root.element("DiskNumber") != null) {
            probe.setDiskNumber(Integer.parseInt(root.element("DiskNumber").getStringValue()));
        }
        if (root.element("Activated") != null) {
            probe.setActivated(root.element("Activated").getStringValue());
        }
        if (root.element("PasswordResetAbility") != null) {
            probe.setPasswordResetAbility(Boolean.parseBoolean(root.element("PasswordResetAbility").getStringValue()));
        }
        if (root.element("PasswordResetModeSecond") != null) {
            probe.setPasswordResetModeSecond(Boolean.parseBoolean(root.element("PasswordResetModeSecond").getStringValue()));
        }
        if (root.element("DeviceLock") != null) {
            probe.setDeviceLock(Boolean.parseBoolean(root.element("DeviceLock").getStringValue()));
        }
        return probe;
    }
    boolean contains(List<ProbeEntity> probeEntityList, ProbeEntity probe) {
        if (probeEntityList == null || probeEntityList.size() < 1 || probe == null) {
            return false;
        }
        boolean contain = false;
        for (ProbeEntity probeEntity : probeEntityList) {
            if (probe.getMac().equals(probeEntity.getMac())) {
                contain = true;
                break;
            }
        }
        return contain;
    }


}

