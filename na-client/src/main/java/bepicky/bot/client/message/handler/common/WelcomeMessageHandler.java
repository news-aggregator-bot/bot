package bepicky.bot.client.message.handler.common;

import bepicky.bot.core.message.button.InlineMarkupBuilder;
import bepicky.bot.core.cmd.CommandManager;
import bepicky.bot.client.message.handler.context.ChatChainLink;
import bepicky.bot.client.message.handler.context.ChatChainManager;
import bepicky.bot.core.message.handler.MessageHandler;
import bepicky.bot.core.message.template.MessageTemplateContext;
import bepicky.bot.client.message.template.TemplateNames;
import bepicky.bot.client.service.IReaderService;
import bepicky.bot.core.message.template.TemplateUtils;
import bepicky.common.domain.dto.ReaderDto;
import bepicky.common.domain.request.ReaderRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.Arrays;

@Component
@Slf4j
public class WelcomeMessageHandler implements MessageHandler {

    public static final String WELCOME = "welcome";

    @Autowired
    private MessageTemplateContext templateContext;

    @Autowired
    protected CommandManager cmdMngr;

    @Autowired
    private IReaderService readerService;

    @Autowired
    private ChatChainManager chainManager;

    @Override
    public BotApiMethod<Message> handle(Message message) {
        ReaderRequest readerRequest = buildReaderRequest(message);
        ReaderDto reader = readerService.register(readerRequest);
        String text = templateContext.processTemplate(
            WELCOME,
            reader.getPrimaryLanguage().getLang(),
            TemplateUtils.params("reader_name", reader.getName())
        );
        ChatChainLink welcomeChainLink = chainManager.welcomeChain(reader.getChatId()).current();
        InlineMarkupBuilder inlineMarkup = new InlineMarkupBuilder();
        String msgText = templateContext.processEmojiTemplate(
            welcomeChainLink.getButtonKey(),
            reader.getLang()
        );
        InlineMarkupBuilder.InlineButton categoriesButton = new InlineMarkupBuilder.InlineButton(
            msgText,
            welcomeChainLink.getCommand()
        );
        inlineMarkup.addButtons(Arrays.asList(categoriesButton));

        return new SendMessage()
            .enableMarkdownV2(true)
            .setChatId(message.getChatId())
            .setReplyMarkup(inlineMarkup.build())
            .enableHtml(true)
            .setText(text);
    }

    @Override
    public String trigger() {
        return "/start";
    }

    private ReaderRequest buildReaderRequest(Message msg) {
        User from = msg.getFrom();
        ReaderRequest rr = new ReaderRequest();
        rr.setChatId(msg.getChatId());
        rr.setUsername(from.getUserName());
        rr.setFirstName(from.getFirstName());
        rr.setLastName(from.getLastName());
        rr.setPrimaryLanguage(from.getLanguageCode());
        rr.setPlatform("TELEGRAM");
        return rr;
    }
}
