package bepicky.bot.core.message.handler;

import bepicky.bot.core.message.EntityType;

public interface EntityCallbackMessageHandler extends CallbackMessageHandler {

    EntityType entityType();
}
