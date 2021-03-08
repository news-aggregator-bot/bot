package bepicky.bot.core.message.handler;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

public interface MessageHandler {

    BotApiMethod<Message> handle(Message message);

    String trigger();
}
