package bepicky.bot.client.message.handler;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

public interface CallbackMessageHandler extends MessageHandler {

    HandleResult handle(Message message, String data);

    @Getter
    @AllArgsConstructor
    class HandleResult {

        private final String text;
        private final InlineKeyboardMarkup markup;
    }
}
