package bepicky.bot.client.message.handler.common;

import bepicky.bot.client.message.EntityType;
import bepicky.bot.client.message.command.ChatCommand;
import bepicky.bot.client.message.command.CommandType;
import bepicky.bot.client.message.handler.CallbackMessageHandler;
import bepicky.bot.client.message.handler.list.NewsSearchMessageHandler;
import bepicky.bot.client.message.template.MessageTemplateContext;
import bepicky.bot.client.service.IReaderService;
import bepicky.common.domain.dto.ReaderDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.Arrays;
import java.util.Optional;

import static bepicky.bot.client.message.template.TemplateUtils.SEARCH_INSTRUCTION;

@Component
public class SearchMessageHandler implements MessageHandler {

    @Autowired
    private NewsSearchMessageHandler newsSearchMessageHandler;

    @Autowired
    private MessageTemplateContext templateContext;

    @Autowired
    private IReaderService readerService;

    @Override
    public BotApiMethod<Message> handle(Message message) {
        String[] keys = message.getText().split(" ");
        Optional<String> key = validateKey(keys);
        if (key.isEmpty()) {
            ReaderDto reader = readerService.find(message.getChatId());
            return new SendMessage()
                .enableMarkdownV2(true)
                .setChatId(message.getChatId())
                .enableHtml(true)
                .setText(templateContext.processTemplate(SEARCH_INSTRUCTION, reader.getLang()));
        }
        ChatCommand cc = ChatCommand.of(CommandType.SEARCH, EntityType.NEWS_NOTE, 1, key.get());
        cc.setChatId(message.getChatId());
        CallbackMessageHandler.HandleResult result = newsSearchMessageHandler.handle(cc);

        return new SendMessage()
            .enableMarkdownV2(true)
            .setChatId(message.getChatId())
            .setReplyMarkup(result.getInline())
            .enableHtml(true)
            .disableWebPagePreview()
            .setText(result.getText());
    }

    private Optional<String> validateKey(String[] keys) {
        if (keys.length == 1) {
            return Optional.empty();
        }
        String key = String.join("", Arrays.copyOfRange(keys, 1, keys.length));
        if (key.length() < 2) {
            return Optional.empty();
        }
        if (key.length() >= 100) {
            return Optional.empty();
        }
        return Optional.of(key);
    }

    @Override
    public String trigger() {
        return "/search";
    }
}
