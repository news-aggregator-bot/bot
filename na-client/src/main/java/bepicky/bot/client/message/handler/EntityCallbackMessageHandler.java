package bepicky.bot.client.message.handler;

import bepicky.bot.client.message.EntityType;

public interface EntityCallbackMessageHandler extends CallbackMessageHandler {

    EntityType entityType();
}
