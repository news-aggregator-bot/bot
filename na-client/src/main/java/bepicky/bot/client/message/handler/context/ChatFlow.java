package bepicky.bot.client.message.handler.context;

import bepicky.bot.client.message.EntityType;
import bepicky.bot.client.message.button.CommandType;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Builder
@Getter
public class ChatFlow {

    @NotBlank
    private final String buttonKey;

    @NotBlank
    private final String msgKey;

    @NotBlank
    private final String command;

    @NotNull
    private final CommandType commandType;

    @NotNull
    private final EntityType entityType;

    private final ChatFlow next;
}
