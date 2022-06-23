package cn.itcast.search;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class SearchStarter {
    public static void main(String[] args) {
        SpringApplication.run(SearchStarter.class, args);
    }
}
