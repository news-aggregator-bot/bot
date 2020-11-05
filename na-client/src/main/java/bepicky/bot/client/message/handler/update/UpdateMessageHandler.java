package bepicky.bot.client.message.handler.update;

import bepicky.bot.client.message.EntityType;
import bepicky.bot.client.message.handler.CallbackMessageHandler;

public interface UpdateMessageHandler extends CallbackMessageHandler {

    EntityType entityType();
}
