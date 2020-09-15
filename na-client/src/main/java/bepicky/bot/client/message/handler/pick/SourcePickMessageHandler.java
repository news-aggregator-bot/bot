package bepicky.bot.client.message.handler.pick;

import bepicky.bot.client.message.handler.AbstractSourceMessageHandler;
import bepicky.bot.client.message.template.TemplateUtils;
import bepicky.common.domain.response.SourceResponse;
import org.springframework.stereotype.Component;

@Component
public class SourcePickMessageHandler extends AbstractSourceMessageHandler implements PickMessageHandler {

    @Override
    protected String textKey() {
        return TemplateUtils.PICK_SOURCE_SUCCESS;
    }

    @Override
    protected SourceResponse handle(Long chatId, Long srcId) {
        return sourceService.pick(chatId, srcId);
    }

}
