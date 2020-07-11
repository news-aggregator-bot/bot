package vlad110kg.news.aggregator.bot.client.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import vlad110kg.news.aggregator.bot.client.router.PickyNewsBot;

import javax.annotation.PostConstruct;

@Configuration
public class PickyBotConfig {

    @Bean
    public PickyNewsBot aggregationBot() {
        return new PickyNewsBot();
    }

    @PostConstruct
    public void initBots() {
        TelegramBotsApi botsApi = new TelegramBotsApi();

        try {
            botsApi.registerBot(aggregationBot());
        } catch (TelegramApiException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }
}
