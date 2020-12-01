package bepicky.bot.client.message.handler.common;

import bepicky.bot.client.message.button.CommandBuilder;
import bepicky.bot.client.message.button.InlineMarkupBuilder;
import bepicky.bot.client.message.handler.context.ChatFlow;
import bepicky.bot.client.message.handler.context.ChatFlowManager;
import bepicky.bot.client.message.template.MessageTemplateContext;
import bepicky.bot.client.message.template.TemplateUtils;
import bepicky.bot.client.service.IReaderService;
import bepicky.common.domain.dto.ReaderDto;
import bepicky.common.domain.request.ReaderRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.Arrays;

@Component
public class WelcomeMessageHandler implements CommonMessageHandler {

    public static final String WELCOME = "welcome";

    @Autowired
    private MessageTemplateContext templateContext;

    @Autowired
    protected CommandBuilder commandBuilder;

    @Autowired
    private IReaderService readerService;

    @Autowired
    private ChatFlowManager flowContext;

    @Override
    public BotApiMethod<Message> handle(Message message) {
        User from = message.getFrom();
        ReaderDto reader = readerService.register(buildReaderRequest(message.getChatId(), from));
        String text = templateContext.processTemplate(
            WELCOME,
            reader.getPrimaryLanguage().getLang(),
            TemplateUtils.params("reader_name", reader.getFirstName())
        );
        ChatFlow welcome = flowContext.welcomeFlow(reader.getChatId());
        InlineMarkupBuilder inlineMarkup = new InlineMarkupBuilder();
        String msgText = templateContext.processEmojiTemplate(
            welcome.getButtonKey(),
            reader.getPrimaryLanguage().getLang()
        );
        InlineMarkupBuilder.InlineButton categoriesButton = InlineMarkupBuilder.InlineButton.builder()
            .text(msgText)
            .command(welcome.getCommand())
            .build();
        inlineMarkup.addButtons(Arrays.asList(categoriesButton));

        return new SendMessage()
            .enableMarkdownV2(true)
            .setChatId(message.getChatId())
            .setReplyMarkup(inlineMarkup.build())
            .setParseMode("html")
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
