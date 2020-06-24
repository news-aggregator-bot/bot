package vlad110kg.news.aggregator.bot.telegram.message.handler;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

public interface CallbackMessageHandler extends MessageHandler {

    BotApiMethod<Message> handle(Message message, String data);
}
