package cn.td.aiot.common.domain;

import java.util.HashMap;

public class TiduResponse extends HashMap<String, Object> {

    private static final long serialVersionUID = -8713837118340960775L;
    
    public TiduResponse message(String message) {
        this.put("message", message);
        return this;
    }

    public TiduResponse data(Object data) {
        this.put("data", data);
        return this;
    }

    @Override
    public TiduResponse put(String key, Object value) {
        super.put(key, value);
        return this;
    }
}
