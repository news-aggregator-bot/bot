package bepicky.bot.client.message.handler.rm;

import bepicky.bot.client.message.handler.AbstractLanguageMessageHandler;
import bepicky.bot.client.message.template.TemplateUtils;
import bepicky.common.domain.response.LanguageResponse;
import org.springframework.stereotype.Component;

@Component
public class LanguageRemoveMessageHandler extends AbstractLanguageMessageHandler implements RemoveMessageHandler {

    @Override
    protected String textKey() {
        return TemplateUtils.REMOVE_LANGUAGE_SUCCESS;
    }

    @Override
    protected LanguageResponse handle(Long chatId, String lang) {
        return languageService.remove(chatId, lang);
    }

}
