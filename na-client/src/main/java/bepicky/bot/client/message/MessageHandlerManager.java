package bepicky.bot.client.message;

import bepicky.bot.client.message.handler.CallbackMessageHandler;
import bepicky.bot.client.message.handler.MessageHandler;
import bepicky.bot.client.message.handler.common.CommonMessageHandler;
import bepicky.bot.client.message.handler.common.HelpMessageHandler;
import bepicky.bot.client.message.handler.list.ListMessageHandler;
import bepicky.bot.client.message.handler.pick.PickMessageHandler;
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

import static bepicky.bot.client.message.button.CommandBuilder.LIST;
import static bepicky.bot.client.message.button.CommandBuilder.PICK;
import static bepicky.bot.client.message.button.CommandBuilder.REMOVE;

@Slf4j
@Component
public class MessageHandlerManager {

    private final Map<String, CommonMessageHandler> commonMessageHandlers;

    private final Map<String, ListMessageHandler> listMessageHandlers;

    private final Map<String, PickMessageHandler> pickMessageHandlers;

    private final Map<String, RemoveMessageHandler> removeMessageHandlers;

    private final Map<String, Function<String, CallbackMessageHandler>> functionContainer;

    private final Map<String, UtilMessageHandler> utilHandlers;

    @Autowired
    public MessageHandlerManager(
        List<CommonMessageHandler> commonMessageHandlers,
        List<ListMessageHandler> listMessageHandlers,
        List<PickMessageHandler> pickMessageHandlers,
        List<RemoveMessageHandler> removeMessageHandlers,
        List<UtilMessageHandler> utilMessageHandler
    ) {
        this.commonMessageHandlers = convert(commonMessageHandlers);
        this.listMessageHandlers = convert(listMessageHandlers);
        this.pickMessageHandlers = convert(pickMessageHandlers);
        this.removeMessageHandlers = convert(removeMessageHandlers);
        this.functionContainer = ImmutableMap.<String, Function<String, CallbackMessageHandler>>builder()
            .put(LIST, this.listMessageHandlers::get)
            .put(PICK, this.pickMessageHandlers::get)
            .put(REMOVE, this.removeMessageHandlers::get)
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
