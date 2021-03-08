package bepicky.bot.core.message;

import bepicky.bot.core.cmd.CommandTranslator;
import bepicky.bot.core.message.handler.IHelpMessageHandler;
import bepicky.bot.core.message.handler.MessageHandler;
import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Slf4j
public class MessageHandlerManager {

    private final Map<String, MessageHandler> commonMessageHandlers;

    private final IHelpMessageHandler helpMessageHandler;

    private final CommandTranslator txt2Cmd;

    public MessageHandlerManager(
        List<MessageHandler> commonMessageHandlers,
        IHelpMessageHandler helpMessageHandler,
        CommandTranslator txt2Cmd
    ) {
        this.commonMessageHandlers = commonMessageHandlers.stream()
            .collect(ImmutableMap.toImmutableMap(MessageHandler::trigger, Function.identity()));
        this.helpMessageHandler = helpMessageHandler;
        this.txt2Cmd = txt2Cmd;
    }

    public BotApiMethod<Message> manage(Message message) {
        String cmd = message.getText().split(" ")[0];
        return getCommonHandler(cmd).handle(message);
    }

    private MessageHandler getCommonHandler(String text) {
        MessageHandler commonMessageHandler = commonMessageHandlers.get(text);
        if (commonMessageHandler != null) {
            return commonMessageHandler;
        }
        String cmd = txt2Cmd.translate(text);
        return cmd != null ? commonMessageHandlers.get(cmd) : helpMessageHandler;
    }

}
