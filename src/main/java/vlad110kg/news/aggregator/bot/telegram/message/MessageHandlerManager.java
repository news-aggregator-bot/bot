package vlad110kg.news.aggregator.bot.telegram.message;

import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import vlad110kg.news.aggregator.bot.telegram.message.handler.CallbackMessageHandler;
import vlad110kg.news.aggregator.bot.telegram.message.handler.MessageHandler;
import vlad110kg.news.aggregator.bot.telegram.message.handler.common.CommonMessageHandler;
import vlad110kg.news.aggregator.bot.telegram.message.handler.list.ListMessageHandler;
import vlad110kg.news.aggregator.bot.telegram.message.handler.pick.PickMessageHandler;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Slf4j
public class MessageHandlerManager {

    private final Map<String, CommonMessageHandler> commonMessageHandlers;

    private final Map<String, ListMessageHandler> listMessageHandlers;

    private final Map<String, PickMessageHandler> pickMessageHandlers;

    private final Map<String, Function<String, CallbackMessageHandler>> functionContainer;

    public MessageHandlerManager(
        Map<String, CommonMessageHandler> commonMessageHandlers,
        Map<String, ListMessageHandler> listMessageHandlers,
        Map<String, PickMessageHandler> pickMessageHandlers
    ) {
        this.commonMessageHandlers = commonMessageHandlers;
        this.listMessageHandlers = listMessageHandlers;
        this.pickMessageHandlers = pickMessageHandlers;
        this.functionContainer = ImmutableMap.<String, Function<String, CallbackMessageHandler>>builder()
            .put("list", listMessageHandlers::get)
            .put("pick", pickMessageHandlers::get)
            .build();
    }

    public BotApiMethod<Message> manage(Message message) {
        CommonMessageHandler commonMessageHandler = commonMessageHandlers.get(message.getText());
        return commonMessageHandler != null ? commonMessageHandler.handle(message) : manageCallback(message, message.getText());
    }

    public BotApiMethod<Message> manageCallback(Message message, String data) {
        String[] split = MessageUtils.parse(data);
        String command = split[0];
        return functionContainer.get(command).apply(split[1]).handle(message, data);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private List<CommonMessageHandler> commonMessageHandlers;
        private List<ListMessageHandler> listMessageHandlers;
        private List<PickMessageHandler> pickMessageHandlers;

        public Builder common(List<CommonMessageHandler> commonMessageHandlers) {
            this.commonMessageHandlers = commonMessageHandlers;
            return this;
        }
        public Builder list(List<ListMessageHandler> listMessageHandlers) {
            this.listMessageHandlers = listMessageHandlers;
            return this;
        }
        public Builder pick(List<PickMessageHandler> pickMessageHandlers) {
            this.pickMessageHandlers = pickMessageHandlers;
            return this;
        }

        public MessageHandlerManager build() {
            return new MessageHandlerManager(
                convert(commonMessageHandlers),
                convert(listMessageHandlers),
                convert(pickMessageHandlers)
            );
        }

        private <T extends MessageHandler> Map<String, T> convert(List<T> handlers) {
            return handlers.stream().collect(ImmutableMap.toImmutableMap(MessageHandler::trigger, Function.identity()));
        }
    }
}
