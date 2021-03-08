package bepicky.bot.client.message.handler;

import bepicky.bot.core.message.EntityType;
import bepicky.bot.core.message.LangUtils;
import bepicky.bot.core.cmd.ChatCommand;
import bepicky.bot.core.cmd.CommandManager;
import bepicky.bot.core.message.handler.EntityCallbackMessageHandler;
import bepicky.bot.core.message.template.MessageTemplateContext;
import bepicky.bot.client.message.template.TemplateNames;
import bepicky.bot.client.service.ILanguageService;
import bepicky.bot.core.message.template.TemplateUtils;
import bepicky.common.domain.response.LanguageResponse;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractLanguageMessageHandler implements EntityCallbackMessageHandler {

    @Autowired
    protected CommandManager cmdMngr;

    @Autowired
    protected MessageTemplateContext templateContext;

    @Autowired
    protected ILanguageService languageService;

    @Override
    public HandleResult handle(ChatCommand cc) {
        String lang = (String) cc.getId();
        LanguageResponse response = handle(cc.getChatId(), lang);
        if (response.isError()) {
            String errorText = templateContext.errorTemplate(LangUtils.DEFAULT);
            // handling
            return new HandleResult(errorText, null);
        }
        String msg = templateContext.processTemplate(
            TemplateNames.getTemplate(commandType(), entityType()),
            response.getReader().getLang(),
            TemplateUtils.name(response.getLanguage().getLocalized())
        );
        return new HandleResult(msg, null);
    }

    @Override
    public EntityType entityType() {
        return EntityType.LANGUAGE;
    }

    protected abstract LanguageResponse handle(Long chatId, String lang);
}
