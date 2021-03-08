package bepicky.bot.admin.config;

import bepicky.bot.admin.PickyNewsAdminBot;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.annotation.PostConstruct;

@Configuration
public class PickyBotConfig {

    @Bean
    public PickyNewsAdminBot adminBot() {
        return new PickyNewsAdminBot();
    }

    @PostConstruct
    public void initBots() {
        TelegramBotsApi botsApi = new TelegramBotsApi();

        try {
            botsApi.registerBot(adminBot());
        } catch (TelegramApiException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }
}
