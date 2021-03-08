package bepicky.bot.client.message.handler.context;

import bepicky.bot.core.message.EntityType;
import bepicky.bot.core.cmd.CommandType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Builder
@Setter
@Getter
public class ChatChainLink {

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

    private int page;
}
