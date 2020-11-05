package bepicky.bot.client.message.handler.common;

import bepicky.bot.client.message.button.CommandBuilder;
import bepicky.bot.client.message.button.MarkupBuilder;
import bepicky.bot.client.message.handler.context.ChatFlow;
import bepicky.bot.client.message.handler.context.ChatFlowManager;
import bepicky.bot.client.message.template.MessageTemplateContext;
import bepicky.bot.client.message.template.TemplateUtils;
import bepicky.bot.client.service.IReaderService;
import bepicky.common.domain.dto.ReaderDto;
import bepicky.common.domain.request.ReaderRequest;
import com.vdurmont.emoji.EmojiParser;
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
        MarkupBuilder markup = new MarkupBuilder();
        String msgText = templateContext.processTemplate(welcome.getButtonKey(), reader.getPrimaryLanguage().getLang());
        MarkupBuilder.Button categoriesButton = MarkupBuilder.Button.builder()
            .text(EmojiParser.parseToUnicode(msgText))
            .command(welcome.getCommand())
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
