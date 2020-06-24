package vlad110kg.news.aggregator.bot.telegram.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import vlad110kg.news.aggregator.bot.telegram.router.NewsAggregationBot;

import javax.annotation.PostConstruct;

@Configuration
public class TelegramBotConfig {

    @Bean
    public NewsAggregationBot aggregationBot() {
        return new NewsAggregationBot();
    }

    @PostConstruct
    public void initBots() {
        TelegramBotsApi botsApi = new TelegramBotsApi();

        try {
            botsApi.registerBot(aggregationBot());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
