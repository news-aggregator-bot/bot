package bepicky.bot.client.message.handler.util;

import bepicky.bot.core.message.button.InlineMarkupBuilder;
import bepicky.bot.core.message.button.InlineMarkupBuilder.InlineButton;
import bepicky.bot.core.cmd.ChatCommand;
import bepicky.bot.core.cmd.CommandManager;
import bepicky.bot.core.cmd.CommandType;
import bepicky.bot.core.message.handler.UtilMessageHandler;
import bepicky.bot.core.message.template.MessageTemplateContext;
import bepicky.bot.client.message.template.TemplateNames;
import bepicky.bot.client.service.IReaderService;
import bepicky.common.domain.dto.ReaderDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;

import static bepicky.bot.core.message.EntityType.CATEGORY;
import static bepicky.bot.core.message.EntityType.LANGUAGE;
import static bepicky.bot.core.message.EntityType.REGION;
import static bepicky.bot.core.message.EntityType.SOURCE;
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
        ReaderDto reader = readerService.settings(cc.getChatId());

        InlineMarkupBuilder m = new InlineMarkupBuilder();

        String lang = reader.getLang();
        String rBtnTxt = templateContext.processTemplate(SETTINGS_REGION, lang);
        String cBtnTxt = templateContext.processTemplate(SETTINGS_CATEGORY, lang);
        String lBtnTxt = templateContext.processTemplate(SETTINGS_LANGUAGE, lang);
        String srcBtnTxt = templateContext.processTemplate(SETTINGS_SOURCE, lang);
        String closeBtnTxt = templateContext.processTemplate(CLOSE, lang);

        InlineButton regionButton = m.button(rBtnTxt, cmdMngr.update(REGION), cmdMngr.list(REGION));
        InlineButton categoryButton = m.button(cBtnTxt, cmdMngr.update(CATEGORY), cmdMngr.list(CATEGORY));
        InlineButton langButton = m.button(lBtnTxt, cmdMngr.update(LANGUAGE), cmdMngr.list(LANGUAGE));
        InlineButton srcButton = m.button(srcBtnTxt, cmdMngr.update(SOURCE), cmdMngr.list(SOURCE));
        InlineButton closeButton = m.button(closeBtnTxt, cmdMngr.util(CommandType.ENABLE_READER));

        String settingsText = templateContext.processTemplate(TemplateNames.SETTINGS, lang);

        Arrays.asList(langButton, regionButton, categoryButton, srcButton, closeButton).forEach(m::addButton);
        return new HandleResult(settingsText, m.build());
    }

    @Override
    public CommandType commandType() {
        return CommandType.SETTINGS;
    }

}
