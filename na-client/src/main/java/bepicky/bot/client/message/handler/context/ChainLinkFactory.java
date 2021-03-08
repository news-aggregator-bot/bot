package bepicky.bot.client.message.handler.context;

import bepicky.bot.core.message.EntityType;
import bepicky.bot.core.cmd.CommandManager;
import bepicky.bot.core.cmd.CommandType;
import bepicky.bot.client.message.template.TemplateNames;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static bepicky.bot.core.message.EntityType.CATEGORY;
import static bepicky.bot.core.message.EntityType.LANGUAGE;
import static bepicky.bot.core.message.EntityType.REGION;
import static bepicky.bot.core.message.EntityType.SOURCE;
import static bepicky.bot.core.message.EntityType.TRANSITION;
import static bepicky.bot.client.message.template.ButtonNames.DIR_NEXT;
import static bepicky.bot.client.message.template.TemplateNames.ENABLE;
import static bepicky.bot.client.message.template.TemplateNames.ENABLE_READER;

@Component
public class ChainLinkFactory {

    @Autowired
    private CommandManager cmdMngr;

    public ChatChainLink listCategories(String msgKey) {
        return link(DIR_NEXT, msgKey, cmdMngr.list(CATEGORY), CATEGORY, CommandType.LIST);
    }

    public ChatChainLink listRegions(String msgKey) {
        return link(DIR_NEXT, msgKey, cmdMngr.list(REGION), REGION, CommandType.LIST);
    }

    public ChatChainLink listLanguages(String msgKey) {
        return link(DIR_NEXT, msgKey, cmdMngr.list(LANGUAGE), LANGUAGE, CommandType.LIST);
    }

    public ChatChainLink listSources(String msgKey) {
        return link(DIR_NEXT, msgKey, cmdMngr.list(SOURCE), SOURCE, CommandType.LIST);
    }

    public ChatChainLink choiceRegion(String msgKey) {
        return link(DIR_NEXT, msgKey, cmdMngr.choice(REGION), REGION, CommandType.CHOICE);
    }

    public ChatChainLink choiceCommon(String msgKey) {
        return link(DIR_NEXT, msgKey, cmdMngr.choice(CATEGORY), CATEGORY, CommandType.CHOICE);
    }

    public ChatChainLink settings() {
        return link(
            DIR_NEXT,
            TemplateNames.SETTINGS,
            cmdMngr.status() + ";" + CommandType.SETTINGS.getKey(),
            TRANSITION,
            CommandType.SETTINGS
        );
    }

    public ChatChainLink getActivateReader() {
        return link(
            ENABLE,
            ENABLE_READER,
            cmdMngr.status() + ";" + CommandType.ENABLE_READER.getKey(),
            TRANSITION,
            CommandType.ENABLE_READER
        );
    }

    private ChatChainLink link(String b, String m, String c, EntityType e, CommandType t) {
        return ChatChainLink.builder()
            .buttonKey(b)
            .msgKey(m)
            .command(c)
            .entityType(e)
            .commandType(t)
            .build();
    }
}
