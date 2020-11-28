package bepicky.bot.client.message.button;


import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ReplyMarkupBuilder {

    private final List<KeyboardRow> keyboardRows = new ArrayList<>();

    public ReplyMarkupBuilder addButtons(List<String> texts) {
        KeyboardRow row = new KeyboardRow();
        keyboardRows.add(row);
        texts.forEach(row::add);
        return this;
    }

    public ReplyMarkupBuilder addButton(String text) {
        addButtons(Arrays.asList(text));
        return this;
    }

    public ReplyKeyboardMarkup build() {
        ReplyKeyboardMarkup markupInline = new ReplyKeyboardMarkup();
        markupInline.setKeyboard(keyboardRows);
        return markupInline;
    }

}
