package vlad110kg.news.aggregator.bot.telegram;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.telegram.telegrambots.ApiContextInitializer;

@SpringBootApplication
@EnableDiscoveryClient
public class TelegramApp {

    public static void main(String[] args) {
        ApiContextInitializer.init();
        SpringApplication.run(TelegramApp.class, args);
    }
}
