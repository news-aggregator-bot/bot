package bepicky.bot.client.controller;

import bepicky.bot.client.message.button.ReplyMarkupBuilder;
import bepicky.bot.client.message.template.MessageTemplateContext;
import bepicky.bot.client.router.PickyNewsBot;
import bepicky.common.domain.dto.ReaderDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@RestController
@Slf4j
public class UtilController {

    @Autowired
    private PickyNewsBot bot;

    @Autowired
    private MessageTemplateContext templateContext;

    @GetMapping("/ping")
    public boolean ping() {
        return true;
    }

    @PutMapping("/refresh/reply-keyboard")
    public void refreshBoard(@RequestBody ReaderDto readerDto) {
        ReplyMarkupBuilder replyMarkup = new ReplyMarkupBuilder();
        String optionsText = templateContext.helpButton(readerDto.getLang());
        replyMarkup.addButton(optionsText);

        try {
            bot.execute(new SendMessage()
                .enableMarkdownV2(true)
                .setChatId(readerDto.getChatId())
                .setText(templateContext.help(readerDto.getLang()))
                .setReplyMarkup(replyMarkup.build())
            );
        } catch (TelegramApiException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

}
