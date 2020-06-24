package vlad110kg.news.aggregator.bot.telegram;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.ApiContextInitializer;

@SpringBootApplication
public class TelegramApp {

    public static void main(String[] args) {
        ApiContextInitializer.init();
        SpringApplication.run(TelegramApp.class, args);
    }
}
