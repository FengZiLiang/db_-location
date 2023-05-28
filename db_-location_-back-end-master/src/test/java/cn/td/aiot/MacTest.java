package cn.td.aiot;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MacTest {
    public static void main(String[] args) throws Exception {
        String a = "[{\"x\":0.07745371073544671,\"y\":4.368885532461475,\"z\":1.5},{\"x\":0.017415025214665647,\"y\":2.5559840683354724,\"z\":1.5},{\"x\":1.6384595342764054,\"y\":2.670724667330794,\"z\":1.5},{\"x\":1.5383950584083932,\"y\":4.460678011657724,\"z\":1.5}]";
        Gson gson = new Gson();
        List<Map<String,Object>> o = gson.fromJson(a, new TypeToken<List<Map<String, Object>>>() {
        }.getType());
        System.out.println(gson.toJson(o));
    }
}

