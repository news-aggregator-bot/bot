package bepicky.bot.client.message.handler.common;

import bepicky.bot.client.message.button.CommandBuilder;
import bepicky.bot.client.message.button.MarkupBuilder;
import bepicky.bot.client.message.template.MessageTemplateContext;
import bepicky.bot.client.message.template.TemplateUtils;
import bepicky.bot.client.service.IReaderService;
import bepicky.common.domain.dto.ReaderDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.Arrays;

import static bepicky.bot.client.message.EntityUtils.CATEGORY;
import static bepicky.bot.client.message.EntityUtils.LANGUAGE;
import static bepicky.bot.client.message.template.TemplateUtils.BUTTON_CATEGORY;
import static bepicky.bot.client.message.template.TemplateUtils.BUTTON_LANGUAGE;

@Component
public class SettingsMessageHandler implements CommonMessageHandler {

    public static final String SETTINGS = "/settings";

    @Autowired
    private CommandBuilder commandBuilder;

    @Autowired
    private MessageTemplateContext templateContext;

    @Autowired
    private IReaderService readerService;

    @Override
    public BotApiMethod<Message> handle(Message message) {
        ReaderDto reader = readerService.disable(message.getChatId());

        MarkupBuilder markup = new MarkupBuilder();

        MarkupBuilder.Button categoryButton = buildButton(
            commandBuilder.list(CATEGORY),
            templateContext.processTemplate(BUTTON_CATEGORY, reader.getLang())
        );
        MarkupBuilder.Button languageButton = buildButton(
            commandBuilder.list(LANGUAGE),
            templateContext.processTemplate(BUTTON_LANGUAGE, reader.getLang())
        );
        String settingsText = templateContext.processTemplate(TemplateUtils.SETTINGS, reader.getLang());

        markup.addButtons(Arrays.asList(languageButton, categoryButton));
        return new SendMessage().setChatId(message.getChatId()).setReplyMarkup(markup.build()).setText(settingsText);
    }

    private MarkupBuilder.Button buildButton(String languageCommand, String buttonLang) {
        return MarkupBuilder.Button.builder()
            .text(buttonLang)
            .command(languageCommand)
            .build();
    }

    @Override
    public String trigger() {
        return SETTINGS;
    }
}
