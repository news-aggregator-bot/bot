package bepicky.bot.client.message.handler;

import bepicky.bot.core.message.EntityType;
import bepicky.bot.core.message.LangUtils;
import bepicky.bot.core.cmd.ChatCommand;
import bepicky.bot.core.cmd.CommandManager;
import bepicky.bot.core.message.handler.EntityCallbackMessageHandler;
import bepicky.bot.core.message.template.MessageTemplateContext;
import bepicky.bot.client.message.template.TemplateNames;
import bepicky.bot.client.service.ISourceService;
import bepicky.bot.core.message.template.TemplateUtils;
import bepicky.common.domain.response.SourceResponse;
import org.springframework.beans.factory.annotation.Autowired;

import static bepicky.bot.core.message.EntityType.SOURCE;

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
        String msg = templateContext.processTemplate(
            TemplateNames.getTemplate(commandType(), entityType()),
            response.getReader().getLang(),
            TemplateUtils.name(response.getSource().getName())
        );
        return new HandleResult(msg, null);
    }

    @Override
    public EntityType entityType() {
        return SOURCE;
    }

    protected abstract SourceResponse handle(Long chatId, Long sourceId);
}
