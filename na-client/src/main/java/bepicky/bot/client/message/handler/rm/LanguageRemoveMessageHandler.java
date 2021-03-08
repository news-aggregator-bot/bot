package bepicky.bot.client.message.handler.rm;

import bepicky.bot.core.cmd.CommandType;
import bepicky.bot.client.message.handler.AbstractLanguageMessageHandler;
import bepicky.common.domain.response.LanguageResponse;
import org.springframework.stereotype.Component;

@Component
public class LanguageRemoveMessageHandler extends AbstractLanguageMessageHandler {

    @Override
    protected LanguageResponse handle(Long chatId, String lang) {
        return languageService.remove(chatId, lang);
    }

    @Override
    public CommandType commandType() {
        return CommandType.REMOVE;
    }
}
