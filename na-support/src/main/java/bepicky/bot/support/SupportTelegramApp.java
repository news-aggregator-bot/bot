package bepicky.bot.support;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.telegram.telegrambots.ApiContextInitializer;

@SpringBootApplication
@EnableFeignClients(basePackages = {"bepicky.bot.support.feign"})
@EnableDiscoveryClient
public class SupportTelegramApp {

    public static void main(String[] args) {
        ApiContextInitializer.init();
        SpringApplication.run(SupportTelegramApp.class, args);
    }
}
