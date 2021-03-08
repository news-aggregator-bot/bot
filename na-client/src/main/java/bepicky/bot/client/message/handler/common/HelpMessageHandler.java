package bepicky.bot.client.message.handler.common;

import bepicky.bot.core.message.button.ReplyMarkupBuilder;
import bepicky.bot.client.message.template.ButtonNames;
import bepicky.bot.core.message.handler.IHelpMessageHandler;
import bepicky.bot.core.message.template.MessageTemplateContext;
import bepicky.bot.client.message.template.TemplateNames;
import bepicky.bot.client.service.IReaderService;
import bepicky.common.domain.dto.ReaderDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
public class HelpMessageHandler implements IHelpMessageHandler {

    public static final String HELP = "/help";

    @Autowired
    private MessageTemplateContext templateContext;

    @Autowired
    private IReaderService readerService;

    @Override
    public BotApiMethod<Message> handle(Message message) {
        ReaderDto reader = readerService.find(message.getChatId());
        String text = templateContext.processTemplate(TemplateNames.HELP, reader.getLang());

        ReplyMarkupBuilder replyMarkup = new ReplyMarkupBuilder();
        String optionsText = templateContext.processTemplate(ButtonNames.HELP, reader.getLang());
        replyMarkup.addButton(optionsText);

        return new SendMessage()
            .setChatId(message.getChatId())
            .setReplyMarkup(replyMarkup.build())
            .setText(text);
    }

    @Override
    public String trigger() {
        return HELP;
    }
}
