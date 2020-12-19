package bepicky.bot.client.message.handler.util;

import bepicky.bot.client.message.button.InlineMarkupBuilder;
import bepicky.bot.client.message.command.ChatCommand;
import bepicky.bot.client.message.command.CommandManager;
import bepicky.bot.client.message.command.CommandType;
import bepicky.bot.client.message.template.MessageTemplateContext;
import bepicky.bot.client.message.template.TemplateUtils;
import bepicky.bot.client.service.IReaderService;
import bepicky.common.domain.dto.ReaderDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
    private CommandManager cmdMngr;

    @Autowired
    private MessageTemplateContext templateContext;

    @Autowired
    private IReaderService readerService;

    @Override
    public HandleResult handle(ChatCommand cc) {
        ReaderDto reader = readerService.disable(cc.getChatId());

        InlineMarkupBuilder markup = new InlineMarkupBuilder();

        String lang = reader.getLang();
        InlineMarkupBuilder.InlineButton regionButton = buildButton(
            cmdMngr.list(REGION),
            SETTINGS_REGION, lang
        );
        InlineMarkupBuilder.InlineButton categoryButton = buildButton(
            cmdMngr.list(CATEGORY),
            SETTINGS_CATEGORY, lang
        );
        InlineMarkupBuilder.InlineButton languageButton = buildButton(
            cmdMngr.list(LANGUAGE),
            SETTINGS_LANGUAGE, lang
        );
        InlineMarkupBuilder.InlineButton sourceButton = buildButton(
            cmdMngr.list(SOURCE),
            SETTINGS_SOURCE, lang
        );
        InlineMarkupBuilder.InlineButton closeButton = buildButton(
            cmdMngr.util(CommandType.ENABLE_READER),
            CLOSE,
            lang
        );

        String settingsText = templateContext.processTemplate(TemplateUtils.SETTINGS, lang);

        Arrays.asList(languageButton, regionButton, categoryButton, sourceButton, closeButton)
            .forEach(markup::addButton);
        return new HandleResult(settingsText, markup.build());
    }

    @Override
    public CommandType commandType() {
        return CommandType.SETTINGS;
    }

    private InlineMarkupBuilder.InlineButton buildButton(String cmd, String textKey, String lang) {
        return new InlineMarkupBuilder.InlineButton(templateContext.processTemplate(textKey, lang), cmd);
    }
}
