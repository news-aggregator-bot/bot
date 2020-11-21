package bepicky.bot.client.message;

import bepicky.bot.client.message.button.CommandType;
import bepicky.bot.client.message.handler.CallbackMessageHandler;
import bepicky.bot.client.message.handler.MessageHandler;
import bepicky.bot.client.message.handler.common.CommonMessageHandler;
import bepicky.bot.client.message.handler.common.HelpMessageHandler;
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

import static bepicky.bot.client.message.button.CommandType.PICK;
import static bepicky.bot.client.message.button.CommandType.PICK_ALL;
import static bepicky.bot.client.message.button.CommandType.REMOVE;
import static bepicky.bot.client.message.button.CommandType.REMOVE_ALL;


@Slf4j
@Component
public class MessageHandlerManager {

    private final Map<String, CommonMessageHandler> commonMessageHandlers;

    private final Map<CommandType, Map<String, ListMessageHandler>> listMessageHandlers;

    private final Map<String, PickMessageHandler> pickMessageHandlers;

    private final Map<String, PickAllMessageHandler> pickAllMessageHandlers;

    private final Map<String, RemoveMessageHandler> removeMessageHandlers;

    private final Map<String, RemoveAllMessageHandler> removeAllMessageHandlers;

    private final Map<String, Function<String, CallbackMessageHandler>> functionContainer;

    private final Map<String, UtilMessageHandler> utilHandlers;

    @Autowired
    public MessageHandlerManager(
        List<CommonMessageHandler> commonMessageHandlers,
        List<ListMessageHandler> listMessageHandlers,
        List<PickMessageHandler> pickMessageHandlers,
        List<PickAllMessageHandler> pickAllMessageHandlers,
        List<RemoveMessageHandler> removeMessageHandlers,
        List<RemoveAllMessageHandler> removeAllMessageHandlers,
        List<UtilMessageHandler> utilMessageHandler
    ) {
        this.commonMessageHandlers = convert(commonMessageHandlers);
        this.pickMessageHandlers = convert(pickMessageHandlers);
        this.pickAllMessageHandlers = convert(pickAllMessageHandlers);
        this.removeMessageHandlers = convert(removeMessageHandlers);
        this.removeAllMessageHandlers = convert(removeAllMessageHandlers);
        this.listMessageHandlers = listMessageHandlers.stream()
            .collect(Collectors.groupingBy(
                ListMessageHandler::commandType,
                Collectors.toMap(MessageHandler::trigger, Function.identity())
            ));
        ImmutableMap.Builder<String, Function<String, CallbackMessageHandler>> functionBuilder =
            ImmutableMap.builder();
        this.listMessageHandlers.forEach((type, handler) -> functionBuilder.put(type.name(), handler::get));
        this.functionContainer = functionBuilder
            .put(PICK.name(), this.pickMessageHandlers::get)
            .put(PICK_ALL.name(), this.pickAllMessageHandlers::get)
            .put(REMOVE.name(), this.removeMessageHandlers::get)
            .put(REMOVE_ALL.name(), this.removeAllMessageHandlers::get)
            .build();

        this.utilHandlers = utilMessageHandler.stream()
            .collect(Collectors.toMap(UtilMessageHandler::trigger, Function.identity()));
    }

    public BotApiMethod<Message> manage(Message message) {
        CommonMessageHandler commonMessageHandler = commonMessageHandlers.getOrDefault(
            message.getText(),
            commonMessageHandlers.get(HelpMessageHandler.HELP)
        );
        return commonMessageHandler.handle(message);
    }

    public BotApiMethod<Serializable> manageCallback(Message message, String data) {
        CallbackMessageHandler.HandleResult handleResult = handleCallback(message, data);
        return new EditMessageText()
            .setChatId(message.getChatId())
            .setMessageId(message.getMessageId())
            .setText(handleResult.getText())
            .setReplyMarkup(handleResult.getMarkup());
    }

    private CallbackMessageHandler.HandleResult handleCallback(Message message, String data) {
        String[] split = MessageUtils.parse(data);
        String command = split[0];
        if (split.length > 1) {
            return functionContainer.get(command).apply(split[1]).handle(message, data);
        }
        return utilHandlers.get(command).handle(message, data);
    }

    private <T extends MessageHandler> Map<String, T> convert(List<T> handlers) {
        return handlers.stream().collect(ImmutableMap.toImmutableMap(MessageHandler::trigger, Function.identity()));
    }
}
