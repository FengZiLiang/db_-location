package cn.td.aiot.common.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Data
@Component
@Configuration
@ConfigurationProperties(prefix = "tidu")
public class TdProperties {

    private ShiroProperties shiro = new ShiroProperties();

    private boolean openAopLog = true;

}
