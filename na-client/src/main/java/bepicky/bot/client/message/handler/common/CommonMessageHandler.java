package bepicky.bot.client.message.handler.common;

import bepicky.bot.client.message.handler.MessageHandler;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

public interface CommonMessageHandler extends MessageHandler {
    BotApiMethod<Message> handle(Message message);
}
