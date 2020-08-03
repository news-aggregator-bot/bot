package bepicky.bot.admin;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

@Slf4j
public class PickyNewsAdminBot extends TelegramLongPollingBot {

    @Value("${bot.name}")
    private String name;

    @Value("${bot.token}")
    private String token;

    @Override
    public void onUpdateReceived(Update update) {
//        if (update.hasMessage()) {
//            try {
//                execute(handlerManager.manage(update.getMessage()));
//            } catch (TelegramApiException e) {
//                log.error(e.getMessage(), e);
//                //TODO: send to admin
//                SendMessage m = new SendMessage()
//                    .setChatId(update.getMessage().getChatId())
//                    .setText(e.getMessage());
//                try {
//                    execute(m);
//                } catch (TelegramApiException ex) {
//                    log.error(ex.getMessage(), ex);
//                }
//            }
//        } else if (update.hasCallbackQuery()) {
//            try {
//                CallbackQuery callbackQuery = update.getCallbackQuery();
//                execute(handlerManager.manageCallback(callbackQuery.getMessage(), callbackQuery.getData()));
//            } catch (TelegramApiException e) {
//                e.printStackTrace();
//            }
//        }
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
