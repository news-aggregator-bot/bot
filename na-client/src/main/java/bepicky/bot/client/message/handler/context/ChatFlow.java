package bepicky.bot.client.message.handler.context;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ChatFlow {

    private final String buttonKey;

    private final String msgKey;

    private final String command;

    private final ChatFlow next;
}
