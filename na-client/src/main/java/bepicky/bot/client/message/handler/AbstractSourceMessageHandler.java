package bepicky.bot.client.message.handler;

import bepicky.bot.client.message.EntityType;
import bepicky.bot.client.message.LangUtils;
import bepicky.bot.client.message.command.ChatCommand;
import bepicky.bot.client.message.command.CommandManager;
import bepicky.bot.client.message.template.MessageTemplateContext;
import bepicky.bot.client.service.ISourceService;
import bepicky.common.domain.response.SourceResponse;
import org.springframework.beans.factory.annotation.Autowired;

import static bepicky.bot.client.message.EntityType.SOURCE;

public abstract class AbstractSourceMessageHandler implements EntityCallbackMessageHandler {

    @Autowired
    protected CommandManager cmdMngr;

    @Autowired
    protected MessageTemplateContext templateContext;

    @Autowired
    protected ISourceService sourceService;

    @Override
    public HandleResult handle(ChatCommand cc) {
        long srcId = (long) cc.getId();
        SourceResponse response = handle(cc.getChatId(), srcId);
        if (response.isError()) {
            String errorText = templateContext.errorTemplate(LangUtils.DEFAULT);
            // handling
            return new HandleResult(errorText, null);
        }
        return new HandleResult();
    }

    @Override
    public EntityType entityType() {
        return SOURCE;
    }

    protected abstract SourceResponse handle(Long chatId, Long sourceId);
}
