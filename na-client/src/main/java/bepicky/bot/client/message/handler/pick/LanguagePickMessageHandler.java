package bepicky.bot.client.message.handler.pick;

import bepicky.bot.client.message.handler.AbstractLanguageMessageHandler;
import bepicky.bot.client.message.template.TemplateUtils;
import bepicky.common.domain.response.LanguageResponse;
import org.springframework.stereotype.Component;

@Component
public class LanguagePickMessageHandler extends AbstractLanguageMessageHandler implements PickMessageHandler {

    @Override
    protected String textKey() {
        return TemplateUtils.PICK_LANGUAGE_SUCCESS;
    }

    @Override
    protected LanguageResponse handle(Long chatId, String lang) {
        return languageService.pick(chatId, lang);
    }
}