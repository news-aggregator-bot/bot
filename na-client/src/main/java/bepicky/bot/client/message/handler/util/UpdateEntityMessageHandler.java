package bepicky.bot.client.message.handler.util;

import bepicky.bot.core.message.EntityType;
import bepicky.bot.core.cmd.ChatCommand;
import bepicky.bot.core.cmd.CommandType;
import bepicky.bot.client.message.handler.context.ChainLinkFactory;
import bepicky.bot.client.message.handler.context.ChatChainLink;
import bepicky.bot.client.message.handler.context.ChatChainManager;
import bepicky.bot.client.message.template.TemplateNames;
import bepicky.bot.core.message.handler.UtilMessageHandler;
import com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;

@Component
public class UpdateEntityMessageHandler implements UtilMessageHandler {

    private Map<EntityType, ChatChainLink> updateLinks;

    @Autowired
    private ChatChainManager chainManager;

    @Autowired
    private ChainLinkFactory linkFactory;

    @PostConstruct
    public void initUpdateContext() {
        this.updateLinks = ImmutableMap.<EntityType, ChatChainLink>builder()
            .put(EntityType.CATEGORY, linkFactory.listCategories(TemplateNames.LIST_CATEGORY))
            .put(EntityType.REGION, linkFactory.listRegions(TemplateNames.LIST_REGIONS))
            .put(EntityType.LANGUAGE, linkFactory.listLanguages(TemplateNames.LIST_LANGUAGES))
            .put(EntityType.SOURCE, linkFactory.listSources(TemplateNames.LIST_SOURCES))
            .build();
    }

    @Override
    public HandleResult handle(ChatCommand command) {
        chainManager.updateChain(command.getChatId(), updateLinks.get(command.getEntityType())).goNext();
        return new HandleResult();
    }

    @Override
    public CommandType commandType() {
        return CommandType.UPDATE;
    }
}
