package bepicky.bot.client.message.handler.context;

import bepicky.bot.client.message.EntityType;
import bepicky.bot.client.message.command.CommandManager;
import bepicky.bot.client.message.command.CommandType;
import bepicky.bot.client.message.template.TemplateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static bepicky.bot.client.message.EntityType.CATEGORY;
import static bepicky.bot.client.message.EntityType.LANGUAGE;
import static bepicky.bot.client.message.EntityType.REGION;
import static bepicky.bot.client.message.EntityType.SOURCE;
import static bepicky.bot.client.message.EntityType.TRANSITION;
import static bepicky.bot.client.message.template.ButtonNames.DIR_NEXT;
import static bepicky.bot.client.message.template.TemplateUtils.ENABLE;
import static bepicky.bot.client.message.template.TemplateUtils.ENABLE_READER;

@Component
public class ChainLinkFactory {

    private final ChatChainLink activateReader = link(
        ENABLE,
        ENABLE_READER,
        CommandType.ENABLE_READER.getKey(),
        TRANSITION,
        CommandType.ENABLE_READER
    );

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

    public ChatChainLink settings() {
        return link(DIR_NEXT, TemplateUtils.SETTINGS, CommandType.SETTINGS.getKey(), TRANSITION, CommandType.SETTINGS);
    }

    public ChatChainLink getActivateReader() {
        return activateReader;
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
