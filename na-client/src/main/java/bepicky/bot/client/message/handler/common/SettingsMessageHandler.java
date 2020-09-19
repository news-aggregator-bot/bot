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
import java.util.stream.Stream;

import static bepicky.bot.client.message.EntityType.CATEGORY;
import static bepicky.bot.client.message.EntityType.LANGUAGE;
import static bepicky.bot.client.message.EntityType.SOURCE;
import static bepicky.bot.client.message.template.TemplateUtils.BUTTON_CATEGORY;
import static bepicky.bot.client.message.template.TemplateUtils.BUTTON_LANGUAGE;
import static bepicky.bot.client.message.template.TemplateUtils.BUTTON_SOURCE;
import static bepicky.bot.client.message.template.TemplateUtils.CLOSE;
import static bepicky.bot.client.message.template.TemplateUtils.ENABLE_READER;

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

        String lang = reader.getLang();
        MarkupBuilder.Button categoryButton = buildButton(commandBuilder.list(CATEGORY.lower()), BUTTON_CATEGORY, lang);
        MarkupBuilder.Button languageButton = buildButton(commandBuilder.list(LANGUAGE.lower()), BUTTON_LANGUAGE, lang);
        MarkupBuilder.Button sourceButton = buildButton(commandBuilder.list(SOURCE.lower()), BUTTON_SOURCE, lang);
        MarkupBuilder.Button closeButton = buildButton(ENABLE_READER, CLOSE, lang);

        String settingsText = templateContext.processTemplate(TemplateUtils.SETTINGS, lang);

        Stream.of(languageButton, categoryButton, sourceButton, closeButton)
            .map(Arrays::asList)
            .forEach(markup::addButtons);
        return new SendMessage().setChatId(message.getChatId()).setReplyMarkup(markup.build()).setText(settingsText);
    }

    private MarkupBuilder.Button buildButton(String languageCommand, String textKey, String lang) {
        String text = templateContext.processTemplate(textKey, lang);
        return MarkupBuilder.Button.builder()
            .text(text)
            .command(languageCommand)
            .build();
    }

    @Override
    public String trigger() {
        return SETTINGS;
    }
}
