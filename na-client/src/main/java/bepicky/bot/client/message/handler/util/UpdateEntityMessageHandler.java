package bepicky.bot.client.message.handler.util;

import bepicky.bot.client.message.EntityType;
import bepicky.bot.client.message.command.ChatCommand;
import bepicky.bot.client.message.command.CommandType;
import bepicky.bot.client.message.handler.context.ChainLinkFactory;
import bepicky.bot.client.message.handler.context.ChatChainLink;
import bepicky.bot.client.message.handler.context.ChatChainManager;
import bepicky.bot.client.message.template.TemplateUtils;
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
            .put(EntityType.CATEGORY, linkFactory.listCategories(TemplateUtils.LIST_CATEGORY))
            .put(EntityType.REGION, linkFactory.listRegions(TemplateUtils.LIST_REGIONS))
            .put(EntityType.LANGUAGE, linkFactory.listLanguages(TemplateUtils.LIST_LANGUAGES))
            .put(EntityType.SOURCE, linkFactory.listSources(TemplateUtils.LIST_SOURCES))
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
