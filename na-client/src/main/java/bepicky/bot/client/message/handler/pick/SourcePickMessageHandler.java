package bepicky.bot.client.message.handler.pick;

import bepicky.bot.core.cmd.CommandType;
import bepicky.bot.client.message.handler.AbstractSourceMessageHandler;
import bepicky.common.domain.response.SourceResponse;
import org.springframework.stereotype.Component;

@Component
public class SourcePickMessageHandler extends AbstractSourceMessageHandler {

    @Override
    protected SourceResponse handle(Long chatId, Long srcId) {
        return sourceService.pick(chatId, srcId);
    }

    @Override
    public CommandType commandType() {
        return CommandType.PICK;
    }
}
