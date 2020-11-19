package bepicky.bot.client.message.handler.list;

import bepicky.bot.client.message.EntityType;
import bepicky.bot.client.message.button.CommandType;
import bepicky.bot.client.message.handler.CallbackMessageHandler;

public interface ListMessageHandler extends CallbackMessageHandler {

    int FOUR_PAGE_SIZE = 4;

    int SIX_PAGE_SIZE = 6;

    CommandType commandType();

    EntityType entityType();
}
