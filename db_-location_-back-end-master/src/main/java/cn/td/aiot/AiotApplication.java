package cn.td.aiot;

import com.google.gson.Gson;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@EnableScheduling
@EnableAsync
public class AiotApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(AiotApplication.class)
                .run(args);
    }

    @Bean
    public Gson gson() {
        return new Gson();
    }

}
