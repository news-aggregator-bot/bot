package bepicky.bot.client.message;

import bepicky.bot.client.message.command.ChatCommand;
import bepicky.bot.client.message.command.CommandType;
import bepicky.bot.client.message.handler.CallbackMessageHandler;
import bepicky.bot.client.message.handler.EntityCallbackMessageHandler;
import bepicky.bot.client.message.handler.MessageToCommandContainer;
import bepicky.bot.client.message.handler.common.HelpMessageHandler;
import bepicky.bot.client.message.handler.common.MessageHandler;
import bepicky.bot.client.message.handler.list.ListMessageHandler;
import bepicky.bot.client.message.handler.pick.PickAllMessageHandler;
import bepicky.bot.client.message.handler.pick.PickMessageHandler;
import bepicky.bot.client.message.handler.rm.RemoveAllMessageHandler;
import bepicky.bot.client.message.handler.rm.RemoveMessageHandler;
import bepicky.bot.client.message.handler.util.UtilMessageHandler;
import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static bepicky.bot.client.message.command.CommandType.PICK;
import static bepicky.bot.client.message.command.CommandType.PICK_ALL;
import static bepicky.bot.client.message.command.CommandType.REMOVE;
import static bepicky.bot.client.message.command.CommandType.REMOVE_ALL;


@Slf4j
@Component
public class MessageHandlerManager {

    private final Map<String, MessageHandler> commonMessageHandlers;

    private final Map<CommandType, Function<EntityType, CallbackMessageHandler>> functionContainer;

    private final Map<CommandType, UtilMessageHandler> utilHandlers;

    private final MessageToCommandContainer msg2CmdContainer;

    @Autowired
    public MessageHandlerManager(
        List<MessageHandler> commonMessageHandlers,
        List<EntityCallbackMessageHandler> entityMessageHandlers,
        List<UtilMessageHandler> utilMessageHandler,
        MessageToCommandContainer msg2CommandContainer
    ) {
        this.commonMessageHandlers = commonMessageHandlers.stream()
            .collect(ImmutableMap.toImmutableMap(MessageHandler::trigger, Function.identity()));

        ImmutableMap.Builder<CommandType, Function<EntityType, CallbackMessageHandler>> functionBuilder =
            ImmutableMap.builder();
        entityMessageHandlers.stream()
            .collect(Collectors.groupingBy(
                EntityCallbackMessageHandler::commandType,
                Collectors.toMap(EntityCallbackMessageHandler::entityType, Function.identity())
            )).forEach((type, handler) -> functionBuilder.put(type, handler::get));
        this.functionContainer = functionBuilder.build();

        this.utilHandlers = utilMessageHandler.stream()
            .collect(Collectors.toMap(UtilMessageHandler::commandType, Function.identity()));
        this.msg2CmdContainer = msg2CommandContainer;
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
        String cmd = msg2CmdContainer.getCommand(text);
        return cmd != null ? commonMessageHandlers.get(cmd) : commonMessageHandlers.get(HelpMessageHandler.HELP);
    }

    public BotApiMethod<Serializable> manageCallback(Message message, String data) {
        StringBuilder txt = new StringBuilder();
        CallbackMessageHandler.HandleResult handleResult = null;
        for (String cc : data.split(";")) {
            handleResult = handleCallback(message.getChatId(), cc);
            if (handleResult != null && handleResult.getText() != null) {
                txt.append("\n").append(handleResult.getText());
            }
        }
        EditMessageText msg = new EditMessageText()
            .setChatId(message.getChatId())
            .setMessageId(message.getMessageId())
            .setText(txt.toString())
            .enableHtml(true)
            .setReplyMarkup(handleResult.getInline());
        if (!handleResult.isPreviewPage()) {
            msg.disableWebPagePreview();
        }
        return msg;
    }

    private CallbackMessageHandler.HandleResult handleCallback(long chatId, String data) {
        ChatCommand cc = ChatCommand.fromText(data);
        cc.setChatId(chatId);
        if (CommandType.UTIL.contains(cc.getCommandType())) {
            return utilHandlers.get(cc.getCommandType()).handle(cc);
        }
        return functionContainer.get(cc.getCommandType()).apply(cc.getEntityType()).handle(cc);
    }

}
