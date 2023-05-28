package cn.td.aiot.location.power95;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class XmlReadUtil {
    public static String readXml(String file) {
        Resource resource = new ClassPathResource("locationTemplate/" + file);
        InputStream is = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        String xmlRes = "";
        try {
            is = resource.getInputStream();
            isr = new InputStreamReader(is);
            br = new BufferedReader(isr);
            String data = null;
            while ((data = br.readLine()) != null) {
//                System.out.println(data);
                xmlRes += data.trim();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
                isr.close();
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return xmlRes.trim()+"\\r\\n";
        }
    }
}
