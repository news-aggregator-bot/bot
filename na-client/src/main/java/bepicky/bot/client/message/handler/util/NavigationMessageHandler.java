package bepicky.bot.client.message.handler.util;

import bepicky.bot.core.cmd.ChatCommand;
import bepicky.bot.client.message.handler.context.ChatChainLink;
import bepicky.bot.client.message.handler.context.ChatChainManager;
import bepicky.bot.core.message.handler.UtilMessageHandler;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class NavigationMessageHandler implements UtilMessageHandler {

    @Autowired
    protected ChatChainManager chainManager;

    @Override
    public HandleResult handle(ChatCommand cc) {
        goDirection(cc.getChatId());
        return new HandleResult();
    }

    protected abstract ChatChainLink goDirection(long chatId);

}
