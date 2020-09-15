package bepicky.bot.client.message.handler.rm;

import bepicky.bot.client.message.handler.AbstractSourceMessageHandler;
import bepicky.bot.client.message.template.TemplateUtils;
import bepicky.common.domain.response.SourceResponse;
import org.springframework.stereotype.Component;

@Component
public class SourceRemoveMessageHandler extends AbstractSourceMessageHandler implements RemoveMessageHandler {

    @Override
    protected String textKey() {
        return TemplateUtils.REMOVE_SOURCE_SUCCESS;
    }

    @Override
    protected SourceResponse handle(Long chatId, Long srcId) {
        return sourceService.remove(chatId, srcId);
    }

}
