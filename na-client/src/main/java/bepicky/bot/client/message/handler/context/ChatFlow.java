package bepicky.bot.client.message.handler.context;

import bepicky.bot.client.message.EntityType;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ChatFlow {

    private final String buttonKey;

    private final String msgKey;

    private final String command;

    private final EntityType type;

    private final ChatFlow next;
}
