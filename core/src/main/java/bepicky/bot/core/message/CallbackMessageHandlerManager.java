package bepicky.bot.core.message;

import bepicky.bot.core.cmd.ChatCommand;
import bepicky.bot.core.cmd.CommandType;
import bepicky.bot.core.message.handler.CallbackMessageHandler;
import bepicky.bot.core.message.handler.EntityCallbackMessageHandler;
import bepicky.bot.core.message.handler.UtilMessageHandler;
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

@Slf4j
public class CallbackMessageHandlerManager {

    private final Map<CommandType, Function<EntityType, CallbackMessageHandler>> functionContainer;

    private final Map<CommandType, UtilMessageHandler> utilHandlers;

    public CallbackMessageHandlerManager(
        List<EntityCallbackMessageHandler> entityMessageHandlers,
        List<UtilMessageHandler> utilMessageHandler
    ) {
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
