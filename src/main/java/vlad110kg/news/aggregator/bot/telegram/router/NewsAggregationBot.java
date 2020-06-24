package vlad110kg.news.aggregator.bot.telegram.router;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import vlad110kg.news.aggregator.bot.telegram.message.MessageHandlerManager;

@Component
@Slf4j
public class NewsAggregationBot extends TelegramLongPollingBot {

    @Value("${bot.telegram.name}")
    private String name;

    @Value("${bot.telegram.token}")
    private String token;

    @Autowired
    private MessageHandlerManager handlerManager;

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            try {
                execute(handlerManager.manage(update.getMessage()));
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
                execute(handlerManager.manageCallback(callbackQuery.getMessage(), callbackQuery.getData()));
            } catch (TelegramApiException e) {
                e.printStackTrace();
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
