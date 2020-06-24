package vlad110kg.news.aggregator.bot.telegram.message.handler.common;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import vlad110kg.news.aggregator.bot.telegram.message.handler.MessageHandler;

public interface CommonMessageHandler extends MessageHandler {
    BotApiMethod<Message> handle(Message message);
}
