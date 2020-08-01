package bepicky.bot.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.telegram.telegrambots.ApiContextInitializer;

@SpringBootApplication
@EnableFeignClients(basePackages = {"bepicky.news.aggregator.bot.admin.feign"})
@EnableDiscoveryClient
public class AdminTelegramApp {

    public static void main(String[] args) {
        ApiContextInitializer.init();
        SpringApplication.run(AdminTelegramApp.class, args);
    }
}
