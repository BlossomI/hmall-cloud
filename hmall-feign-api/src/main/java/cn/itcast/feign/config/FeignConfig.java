package cn.itcast.feign.config;

import feign.Logger;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(value = "cn.itcast.feign.client",defaultConfiguration = FeignConfig.class)
public class FeignConfig {
    @Bean
    public Logger.Level level(){
        // 打印最详细的feign日志
        return Logger.Level.FULL;
    }
}
