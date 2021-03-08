package bepicky.bot.core;

import bepicky.bot.core.message.CallbackMessageHandlerManager;
import bepicky.bot.core.message.MessageHandlerManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
public class BotRouter extends TelegramLongPollingBot {

    @Value("${bot.telegram.name}")
    private String name;

    @Value("${bot.telegram.token}")
    private String token;

    @Autowired
    private MessageHandlerManager msgManager;

    @Autowired
    private CallbackMessageHandlerManager callbackManager;

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            try {
                execute(msgManager.manage(update.getMessage()));
            } catch (TelegramApiException e) {
                log.error(e.getMessage(), e);
                //TODO: send to admin
                SendMessage m = new SendMessage()
                    .setChatId(update.getMessage().getChatId())
                    .setText(e.getMessage());
                try {
                    execute(m);
                } catch (TelegramApiException ex) {
                    log.error(ex.getMessage(), ex);
                }
            }
        } else if (update.hasCallbackQuery()){
            try {
                CallbackQuery callbackQuery = update.getCallbackQuery();
                execute(callbackManager.manageCallback(callbackQuery.getMessage(), callbackQuery.getData()));
            } catch (TelegramApiException e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    @Override
    public String getBotUsername() {
        return name;
    }

    @Override
    public String getBotToken() {
        return token;
    }
}
