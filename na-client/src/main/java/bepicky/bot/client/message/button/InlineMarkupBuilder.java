package bepicky.bot.client.message.button;


import lombok.Builder;
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
            .map(b -> new InlineKeyboardButton().setText(b.text).setCallbackData(b.command))
            .collect(Collectors.toList()));
        return this;
    }

    public InlineMarkupBuilder addButton(InlineButton button) {
        addButtons(Arrays.asList(button));
        return this;
    }

    public InlineButton button(String text, String command) {
        return InlineButton.builder().text(text).command(command).build();
    }

    public InlineButton done(String text) {
        return InlineButton.builder().text(text).command(CommandType.TRANSITION.name()).build();
    }

    public InlineKeyboardMarkup build() {
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        markupInline.setKeyboard(rowsInline);
        return markupInline;
    }

    @Builder
    @Getter
    public static class InlineButton {

        private final String text;
        private final String command;
    }
}
