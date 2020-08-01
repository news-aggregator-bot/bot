package bepicky.bot.client.config;

import bepicky.bot.client.message.MessageHandlerManager;
import bepicky.bot.client.message.handler.pick.PickMessageHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import bepicky.bot.client.message.handler.common.CommonMessageHandler;
import bepicky.bot.client.message.handler.list.ListMessageHandler;

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
