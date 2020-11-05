package bepicky.bot.client.message.handler.context;

import bepicky.bot.client.message.EntityType;
import bepicky.bot.client.message.button.CommandBuilder;
import bepicky.bot.client.message.button.CommandType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static bepicky.bot.client.message.EntityType.CATEGORY;
import static bepicky.bot.client.message.EntityType.LANGUAGE;
import static bepicky.bot.client.message.EntityType.REGION;
import static bepicky.bot.client.message.EntityType.TRANSITION;
import static bepicky.bot.client.message.template.ButtonNames.DIR_NEXT;
import static bepicky.bot.client.message.template.TemplateUtils.ENABLE;
import static bepicky.bot.client.message.template.TemplateUtils.ENABLE_READER;

@Component
public class ChatFlowFactory {

    private final ChatFlow activateReader = flow(
        ENABLE,
        ENABLE_READER,
        CommandType.ENABLE_READER.name(),
        TRANSITION,
        CommandType.ENABLE_READER
    );

    @Autowired
    private CommandBuilder commandBuilder;

    public ChatFlow listCategories(String msgKey) {
        return listCategories(msgKey, activateReader);
    }

    public ChatFlow listCategories(String msgKey, ChatFlow next) {
        return flow(DIR_NEXT, msgKey, commandBuilder.list(CATEGORY.low()), CATEGORY, CommandType.LIST, next);
    }

    public ChatFlow listRegions(String msgKey, ChatFlow next) {
        return flow(DIR_NEXT, msgKey, commandBuilder.list(REGION.low()), REGION, CommandType.LIST, next);
    }

    public ChatFlow listLanguages(String msgKey) {
        return listLanguages(msgKey, activateReader);
    }

    public ChatFlow listLanguages(String msgKey, ChatFlow next) {
        return flow(DIR_NEXT, msgKey, commandBuilder.list(LANGUAGE.low()), LANGUAGE, CommandType.LIST, next);
    }

    public ChatFlow flow(String msgKey, EntityType e, CommandType t, ChatFlow next) {
        return flow(DIR_NEXT, msgKey, commandBuilder.list(t, e), e, t, next);
    }

    public ChatFlow getActivateReader() {
        return activateReader;
    }

    private ChatFlow flow(String b, String m, String c, EntityType e, CommandType t) {
        return flow(b, m, c, e, t, null);
    }

    private ChatFlow flow(String b, String m, String c, EntityType e, CommandType t, ChatFlow f) {
        return ChatFlow.builder()
            .buttonKey(b)
            .msgKey(m)
            .command(c)
            .entityType(e)
            .commandType(t)
            .next(f)
            .build();
    }
}
