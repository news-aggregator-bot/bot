package vlad110kg.news.aggregator.bot.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.telegram.telegrambots.ApiContextInitializer;

@SpringBootApplication
@EnableFeignClients(basePackages = {"vlad110kg.news.aggregator.bot.client.feign"})
@EnableDiscoveryClient
public class ClientTelegramApp {

    public static void main(String[] args) {
        ApiContextInitializer.init();
        SpringApplication.run(ClientTelegramApp.class, args);
    }
}