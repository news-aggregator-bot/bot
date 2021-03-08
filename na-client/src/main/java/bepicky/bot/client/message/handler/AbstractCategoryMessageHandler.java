package bepicky.bot.client.message.handler;

import bepicky.bot.core.message.LangUtils;
import bepicky.bot.core.cmd.ChatCommand;
import bepicky.bot.core.cmd.CommandManager;
import bepicky.bot.client.message.handler.context.ChatChainManager;
import bepicky.bot.core.message.handler.EntityCallbackMessageHandler;
import bepicky.bot.core.message.template.MessageTemplateContext;
import bepicky.bot.client.message.template.TemplateNames;
import bepicky.bot.client.service.ICategoryService;
import bepicky.bot.core.message.template.TemplateUtils;
import bepicky.common.domain.response.CategoryResponse;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractCategoryMessageHandler implements EntityCallbackMessageHandler {

    @Autowired
    protected CommandManager cmdMngr;

    @Autowired
    protected MessageTemplateContext templateContext;

    @Autowired
    protected ICategoryService categoryService;

    @Autowired
    protected ChatChainManager chainManager;

    @Override
    public HandleResult handle(ChatCommand cc) {
        long categoryId = (long) cc.getId();
        CategoryResponse response = handle(cc.getChatId(), categoryId);
        if (response.isError()) {
            String errorText = templateContext.errorTemplate(LangUtils.DEFAULT);
            return new HandleResult(errorText, null);
        }
        String msg = templateContext.processTemplate(
            TemplateNames.getTemplate(commandType(), entityType()),
            response.getReader().getLang(),
            TemplateUtils.name(response.getCategory().getLocalised())
        );
        return new HandleResult(msg, null);
    }

    protected abstract CategoryResponse handle(Long chatId, Long categoryId);

}
