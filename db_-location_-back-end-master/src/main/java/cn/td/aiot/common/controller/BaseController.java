package cn.td.aiot.common.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaseController {

    protected Map<String, Object> getDataTable(IPage<?> pageInfo) {
        Map<String, Object> rspData = new HashMap<>();
        rspData.put("rows", pageInfo.getRecords());
        rspData.put("total", pageInfo.getTotal());
        return rspData;
    }


    protected Map<String, Object> getDataTable(IPage<?> pageInfo, List<Map<String,Object>> extraData,String extraDataName) {
        Map<String, Object> rspData = new HashMap<>(12);
        rspData.put("rows", pageInfo.getRecords());
        rspData.put("total", pageInfo.getTotal());
        rspData.put(extraDataName, extraData);
        return rspData;
    }

    protected Map<String, Object> getDataMap(Object value, String label) {
        Map<String, Object> map = new HashMap<>(12);
        map.put("value", value);
        map.put("label", label);
        return map;
    }

}
