package bepicky.bot.client.message.handler.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;
import bepicky.bot.client.domain.Reader;
import bepicky.bot.client.domain.request.ReaderRequest;
import bepicky.bot.client.message.button.CommandBuilder;
import bepicky.bot.client.message.button.MarkupBuilder;
import bepicky.bot.client.message.handler.list.CategoryListMessageHandler;
import bepicky.bot.client.message.template.MessageTemplateContext;
import bepicky.bot.client.message.template.TemplateUtils;
import bepicky.bot.client.service.IReaderService;

import java.util.Arrays;

import static bepicky.bot.client.message.template.TemplateUtils.WELCOME_LIST_CATEGORY;

@Component
public class WelcomeMessageHandler implements CommonMessageHandler {

    public static final String WELCOME = "welcome";

    @Autowired
    private MessageTemplateContext templateContext;

    @Autowired
    protected CommandBuilder commandBuilder;

    @Autowired
    private IReaderService readerService;

    @Override
    public BotApiMethod<Message> handle(Message message) {
        User from = message.getFrom();
        Reader reader = readerService.register(buildReaderRequest(message.getChatId(), from));
        String text = templateContext.processTemplate(
            WELCOME,
            reader.getPrimaryLanguage().getLang(),
            TemplateUtils.params("reader_name", reader.getFirstName())
        );
        MarkupBuilder markup = new MarkupBuilder();
        MarkupBuilder.Button categoriesButton = MarkupBuilder.Button.builder()
            .text(templateContext.processTemplate(WELCOME_LIST_CATEGORY, reader.getPrimaryLanguage().getLang()))
            .command(commandBuilder.list(CategoryListMessageHandler.CATEGORY))
            .build();
        markup.addButtons(Arrays.asList(categoriesButton));

        return new SendMessage()
            .setChatId(message.getChatId())
            .setReplyMarkup(markup.build())
            .setText(text);
    }

    @Override
    public String trigger() {
        return "/start";
    }

    private ReaderRequest buildReaderRequest(long chatId, User from) {
        ReaderRequest rr = new ReaderRequest();
        rr.setChatId(chatId);
        rr.setUsername(from.getUserName());
        rr.setFirstName(from.getFirstName());
        rr.setLastName(from.getLastName());
        rr.setPrimaryLanguage(from.getLanguageCode());
        rr.setPlatform("TELEGRAM");
        return rr;
    }
}
