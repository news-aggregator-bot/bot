package bepicky.bot.client.message.button;


import lombok.AllArgsConstructor;
import lombok.Getter;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class InlineMarkupBuilder {

    private final List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();

    public InlineMarkupBuilder addButtons(List<InlineButton> buttons) {
        rowsInline.add(buttons.stream()
            .map(b -> new InlineKeyboardButton().setText(b.text).setCallbackData(String.join(";", b.commands)))
            .collect(Collectors.toList()));
        return this;
    }

    public InlineMarkupBuilder addButton(InlineButton button) {
        addButtons(Arrays.asList(button));
        return this;
    }

    public InlineButton button(String text, String... commands) {
        return new InlineButton(text, Arrays.asList(commands));
    }

    public InlineKeyboardMarkup build() {
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        markupInline.setKeyboard(rowsInline);
        return markupInline;
    }

    @Getter
    @AllArgsConstructor
    public static class InlineButton {

        private final String text;
        private final List<String> commands;


        public InlineButton(String text, String command) {
            this.text = text;
            this.commands = Arrays.asList(command);
        }
    }
}
