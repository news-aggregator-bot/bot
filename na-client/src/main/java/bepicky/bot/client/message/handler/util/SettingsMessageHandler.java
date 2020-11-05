package bepicky.bot.client.message.handler.util;

import bepicky.bot.client.message.button.CommandBuilder;
import bepicky.bot.client.message.button.CommandType;
import bepicky.bot.client.message.button.MarkupBuilder;
import bepicky.bot.client.message.template.MessageTemplateContext;
import bepicky.bot.client.message.template.TemplateUtils;
import bepicky.bot.client.service.IReaderService;
import bepicky.common.domain.dto.ReaderDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.Arrays;

import static bepicky.bot.client.message.EntityType.CATEGORY;
import static bepicky.bot.client.message.EntityType.LANGUAGE;
import static bepicky.bot.client.message.EntityType.REGION;
import static bepicky.bot.client.message.EntityType.SOURCE;
import static bepicky.bot.client.message.template.ButtonNames.CLOSE;
import static bepicky.bot.client.message.template.ButtonNames.SETTINGS_CATEGORY;
import static bepicky.bot.client.message.template.ButtonNames.SETTINGS_LANGUAGE;
import static bepicky.bot.client.message.template.ButtonNames.SETTINGS_REGION;
import static bepicky.bot.client.message.template.ButtonNames.SETTINGS_SOURCE;
import static bepicky.bot.client.message.template.TemplateUtils.ENABLE_READER;

@Component
public class SettingsMessageHandler implements UtilMessageHandler {

    @Autowired
    private CommandBuilder commandBuilder;

    @Autowired
    private MessageTemplateContext templateContext;

    @Autowired
    private IReaderService readerService;

    @Override
    public String trigger() {
        return CommandType.SETTINGS.name();
    }

    @Override
    public HandleResult handle(Message message, String data) {
        ReaderDto reader = readerService.disable(message.getChatId());

        MarkupBuilder markup = new MarkupBuilder();

        String lang = reader.getLang();
        MarkupBuilder.Button regionButton = buildButton(commandBuilder.update(REGION),
            SETTINGS_REGION, lang
        );
        MarkupBuilder.Button categoryButton = buildButton(commandBuilder.update(CATEGORY),
            SETTINGS_CATEGORY, lang
        );
        MarkupBuilder.Button languageButton = buildButton(commandBuilder.list(LANGUAGE.low()),
            SETTINGS_LANGUAGE, lang
        );
        MarkupBuilder.Button sourceButton = buildButton(commandBuilder.list(SOURCE.low()),
            SETTINGS_SOURCE, lang
        );
        MarkupBuilder.Button closeButton = buildButton(ENABLE_READER, CLOSE, lang);

        String settingsText = templateContext.processTemplate(TemplateUtils.SETTINGS, lang);

        Arrays.asList(languageButton, regionButton, categoryButton, sourceButton, closeButton)
            .forEach(markup::addButton);
        return new HandleResult(settingsText, markup.build());
    }

    private MarkupBuilder.Button buildButton(String languageCommand, String textKey, String lang) {
        String text = templateContext.processTemplate(textKey, lang);
        return MarkupBuilder.Button.builder()
            .text(text)
            .command(languageCommand)
            .build();
    }
}
