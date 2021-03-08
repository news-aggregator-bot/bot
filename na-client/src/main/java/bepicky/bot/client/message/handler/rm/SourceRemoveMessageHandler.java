package bepicky.bot.client.message.handler.rm;

import bepicky.bot.core.cmd.CommandType;
import bepicky.bot.client.message.handler.AbstractSourceMessageHandler;
import bepicky.common.domain.response.SourceResponse;
import org.springframework.stereotype.Component;

@Component
public class SourceRemoveMessageHandler extends AbstractSourceMessageHandler {

    @Override
    protected SourceResponse handle(Long chatId, Long srcId) {
        return sourceService.remove(chatId, srcId);
    }

    @Override
    public CommandType commandType() {
        return CommandType.REMOVE;
    }
}
