package vlad110kg.news.aggregator.bot.client.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import vlad110kg.news.aggregator.bot.client.message.MessageHandlerManager;
import vlad110kg.news.aggregator.bot.client.message.handler.common.CommonMessageHandler;
import vlad110kg.news.aggregator.bot.client.message.handler.list.ListMessageHandler;
import vlad110kg.news.aggregator.bot.client.message.handler.pick.PickMessageHandler;

import java.util.List;

@Configuration
public class MessageHandlerConfiguration {
    @Bean
    public MessageHandlerManager handlerManager(List<CommonMessageHandler> commonHandlers, List<ListMessageHandler> listHandlers, List<PickMessageHandler> pickHandlers) {
        return MessageHandlerManager.builder()
            .common(commonHandlers)
            .list(listHandlers)
            .pick(pickHandlers)
            .build();
    }
}
