package bepicky.bot.client.message.handler.common;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
public class HelpMessageHandler implements CommonMessageHandler {

    public static final String HELP = "/help";

    @Override
    public BotApiMethod<Message> handle(Message message) {
        return new SendMessage().setChatId(message.getChatId()).setText("help");
    }

    @Override
    public String trigger() {
        return HELP;
    }
}
