package bepicky.bot.client.message.handler.util;

import bepicky.bot.core.cmd.CommandType;
import bepicky.bot.client.message.handler.context.ChatChainLink;
import org.springframework.stereotype.Component;

@Component
public class PreviousMessageHandler extends NavigationMessageHandler {

    @Override
    protected ChatChainLink goDirection(long chatId) {
        return chainManager.getChain(chatId).goPrevious();
    }

    @Override
    public CommandType commandType() {
        return CommandType.GO_PREVIOUS;
    }
}
