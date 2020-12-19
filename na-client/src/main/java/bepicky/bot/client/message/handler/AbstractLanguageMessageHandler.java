package bepicky.bot.client.message.handler;

import bepicky.bot.client.message.EntityType;
import bepicky.bot.client.message.LangUtils;
import bepicky.bot.client.message.command.ChatCommand;
import bepicky.bot.client.message.command.CommandManager;
import bepicky.bot.client.message.template.MessageTemplateContext;
import bepicky.bot.client.service.ILanguageService;
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
        return new HandleResult();
    }

    @Override
    public EntityType entityType() {
        return EntityType.LANGUAGE;
    }

    protected abstract LanguageResponse handle(Long chatId, String lang);
}
