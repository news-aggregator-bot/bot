package bepicky.bot.client.message.handler;

import bepicky.bot.client.message.command.ChatCommand;
import bepicky.bot.client.message.command.CommandType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

public interface CallbackMessageHandler {

    HandleResult handle(ChatCommand command);

    CommandType commandType();

    @Getter
    @AllArgsConstructor
    class HandleResult {

        private final String text;
        private final InlineKeyboardMarkup inline;

        public HandleResult() {
            this.text = null;
            this.inline = null;
        }
    }
}
