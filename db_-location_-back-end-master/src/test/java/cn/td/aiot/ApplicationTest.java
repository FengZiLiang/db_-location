package cn.td.aiot;

import cn.td.aiot.common.utils.ByteChangeUtil;
import cn.td.aiot.location.action.EnclosureAction;
import cn.td.aiot.location.service.EnclosureService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * ClassName  ApplicationTest <br/>
 * Description  <br/>
 *
 * @author YeYouGui
 * @version 1.0
 * @date 2020/7/16 17:42<br/>
 * @since JDK 1.8
 */
@SpringBootTest(classes = AiotApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@RunWith(SpringRunner.class)
public class ApplicationTest {
    String serverName = "192.168.1.109";
    Integer dataPort = 3335;
    Integer comPort = 3334;
    @Autowired
    EnclosureService enclosureService;

    @Test
    public void getBaseStation() throws IOException {
//        String serverName = "192.168.1.109";
//        String commPort = "3334";
//        String commend = XmlReadUtil.readXml("getBases.xml");
//
//        System.out.println("发送命令：" + commend);
//        SocketCommClient commClient = new SocketCommClient();
//        // 发送命令
//        commClient.doWork(commend, serverName, commPort);
    }

    @Test
    public void read() {


        try {
            Socket socket = new Socket(serverName, dataPort);

            InputStream inputStream = socket.getInputStream();
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            byte[] buff = new byte[1024];
            while (bufferedInputStream.read(buff) != -1) {
                if ("65".equalsIgnoreCase(Long.toHexString(buff[0]).toUpperCase())) {
                    byte[] buffx = {buff[1], buff[2], buff[3], buff[4], buff[5], buff[6], buff[7], buff[8]};
                    byte[] buffy = {buff[9], buff[10], buff[11], buff[12], buff[13], buff[14], buff[15], buff[16]};
                    byte[] buffz = {buff[17], buff[18], buff[19], buff[20], buff[21], buff[22], buff[23], buff[24]};
                    byte[] buffmac = {buff[25], buff[26], buff[27], buff[28], buff[29], buff[30], buff[31], buff[32]};
                    double locationX = Double.parseDouble(String.format("%.2f", ByteChangeUtil.bytes2Double(buffx)));
                    double locationY = Double.parseDouble(String.format("%.2f", ByteChangeUtil.bytes2Double(buffy)));
                    double locationZ = Double.parseDouble(String.format("%.2f", ByteChangeUtil.bytes2Double(buffz)));


                    String device_mac = ByteChangeUtil.byteToHex(buffmac);

                    Map<String, Object> map = new HashMap<>();
                    map.put("x", locationX);
                    map.put("y", locationY);
                    map.put("z", locationZ);
                    map.put("mac", device_mac);

//                    testIsBand(map);
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



//    // 查看是否在这个区域
//    public void testIsBand(Map<String, Object> pt) {
//        QueryWrapper<Enclosure> enclosureQueryWrapper = new QueryWrapper<>();
//        enclosureQueryWrapper.select("enclosure_location", "enclosure_name");
//        List<Enclosure> list = enclosureService.list(enclosureQueryWrapper);
//        Gson gson = new Gson();
//        list.forEach(enclosure -> {
//            String enclosureLocation = enclosure.getEnclosureLocation();
//            List<Map<String, Object>> maps = gson.fromJson(enclosureLocation, new TypeToken<List<Map<String, Object>>>() {
//            }.getType());
//            for (Map<String, Object> map : maps) {
//                double x = (double) map.get("x");
//                double y = (double) map.get("y");
//                double z = (double) map.get("z");
//                System.out.println("*****************************");
//                System.out.println("电子围栏" + x);
//                System.out.println("电子围栏" + y);
//                System.out.println("电子围栏" + z);
//                System.out.println("*****************************");
//
//            }
//
//            double x = (double) pt.get("x");
//            double y = (double) pt.get("y");
//            double z = (double) pt.get("z");
//            System.out.println("*****************************");
//            System.out.println("设备坐标" + x);
//            System.out.println("设备坐标" + y);
//            System.out.println("设备坐标" + z);
//            System.out.println("*****************************");
//            EnterEnclosureActionFactory factory = new EnterEnclosureActionFactory();
//
//
//            EnclosureAction actionHandler = factory.getActionHandler("0");
//            actionHandler.enterAction(pt, maps);
//            boolean inAread = actionHandler.enterAction(pt, maps);
//
////            if (inAread) {
////                System.out.println("进入电子围栏" + "------" + "id：" + enclosure.getEnclosureName());
////            } else {
////                System.out.println("离开电子围栏" + "------" + "id：" + enclosure.getEnclosureName());
////
////            }
//            System.out.println("^^^^^^^^^^^^^^^^^^^^^^");
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        });
//    }


    @Test
    public void nioTest() {
        try {
            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(1024);
            SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress(serverName, dataPort));
            // 开启非阻塞式IO
            socketChannel.configureBlocking(false);
            while (true) {
                socketChannel.write(byteBuffer);
                byteBuffer.clear();
                if (socketChannel.read(byteBuffer) > 0) {
                    byteBuffer.flip();
                    byte[] buff = new byte[byteBuffer.remaining()];
                    byteBuffer.get(buff);
                    if ("65".equalsIgnoreCase(Long.toHexString(buff[0]).toUpperCase())) {

                        byte[] buffx = Arrays.copyOfRange(buff, 1, 9);
                        byte[] buffy = Arrays.copyOfRange(buff, 9, 17);
                        byte[] buffz = Arrays.copyOfRange(buff, 17, 25);
                        byte[] buffmac = Arrays.copyOfRange(buff, 25, 33);
//                        byte[] buffx = {buff[1], buff[2], buff[3], buff[4], buff[5], buff[6], buff[7], buff[8]};
//                        byte[] buffy = {buff[9], buff[10], buff[11], buff[12], buff[13], buff[14], buff[15], buff[16]};
//                        byte[] buffz = {buff[17], buff[18], buff[19], buff[20], buff[21], buff[22], buff[23], buff[24]};
//                        byte[] buffmac = {buff[25], buff[26], buff[27], buff[28], buff[29], buff[30], buff[31], buff[32]};
                        double locationX = Double.parseDouble(String.format("%.2f", ByteChangeUtil.bytes2Double(buffx)));
                        double locationY = Double.parseDouble(String.format("%.2f", ByteChangeUtil.bytes2Double(buffy)));
                        double locationZ = Double.parseDouble(String.format("%.2f", ByteChangeUtil.bytes2Double(buffz)));

                        String device_mac = ByteChangeUtil.byteToHex(buffmac);
                        System.out.println("x轴：" + locationX);
                        System.out.println("Y轴：" + locationY);
                        System.out.println("Z轴：" + locationZ);
                        System.out.println("mac:" + device_mac);
                        System.out.println("***********************************");
                    }
                    byteBuffer.clear();
                }
            }
        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}
