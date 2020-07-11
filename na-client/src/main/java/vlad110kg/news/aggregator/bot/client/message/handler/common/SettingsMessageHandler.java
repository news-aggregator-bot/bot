package vlad110kg.news.aggregator.bot.client.message.handler.common;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
public class SettingsMessageHandler implements CommonMessageHandler {

    @Override
    public BotApiMethod<Message> handle(Message message) {
        return null;
    }

    @Override
    public String trigger() {
        return "/settings";
    }
}
