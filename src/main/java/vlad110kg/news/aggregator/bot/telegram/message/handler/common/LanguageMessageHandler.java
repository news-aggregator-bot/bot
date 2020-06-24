package vlad110kg.news.aggregator.bot.telegram.message.handler.common;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
public class LanguageMessageHandler implements CommonMessageHandler {
    public static final String LANGUAGE = "language";

    @Override
    public BotApiMethod<Message> handle(Message message) {
        return null;
    }

    @Override
    public String trigger() {
        return "pick_language";
    }
}
