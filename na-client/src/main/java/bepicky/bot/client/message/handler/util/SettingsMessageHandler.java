package bepicky.bot.client.message.handler.util;

import bepicky.bot.client.message.button.CommandBuilder;
import bepicky.bot.client.message.button.CommandType;
import bepicky.bot.client.message.button.InlineMarkupBuilder;
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

        InlineMarkupBuilder markup = new InlineMarkupBuilder();

        String lang = reader.getLang();
        InlineMarkupBuilder.InlineButton regionButton = buildButton(commandBuilder.list(REGION.low()),
            SETTINGS_REGION, lang
        );
        InlineMarkupBuilder.InlineButton categoryButton = buildButton(commandBuilder.list(CATEGORY.low()),
            SETTINGS_CATEGORY, lang
        );
        InlineMarkupBuilder.InlineButton languageButton = buildButton(commandBuilder.list(LANGUAGE.low()),
            SETTINGS_LANGUAGE, lang
        );
        InlineMarkupBuilder.InlineButton sourceButton = buildButton(commandBuilder.list(SOURCE.low()),
            SETTINGS_SOURCE, lang
        );
        InlineMarkupBuilder.InlineButton closeButton = buildButton(CommandType.ENABLE_READER.name(), CLOSE, lang);

        String settingsText = templateContext.processTemplate(TemplateUtils.SETTINGS, lang);

        Arrays.asList(languageButton, regionButton, categoryButton, sourceButton, closeButton)
            .forEach(markup::addButton);
        return new HandleResult(settingsText, markup.build());
    }

    private InlineMarkupBuilder.InlineButton buildButton(String languageCommand, String textKey, String lang) {
        String text = templateContext.processTemplate(textKey, lang);
        return InlineMarkupBuilder.InlineButton.builder()
            .text(text)
            .command(languageCommand)
            .build();
    }
}
