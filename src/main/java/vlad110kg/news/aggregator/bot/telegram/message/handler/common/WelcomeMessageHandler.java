package vlad110kg.news.aggregator.bot.telegram.message.handler.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;
import vlad110kg.news.aggregator.bot.telegram.domain.Reader;
import vlad110kg.news.aggregator.bot.telegram.domain.request.ReaderRequest;
import vlad110kg.news.aggregator.bot.telegram.message.template.MessageTemplateContext;
import vlad110kg.news.aggregator.bot.telegram.message.template.TemplateUtils;
import vlad110kg.news.aggregator.bot.telegram.service.IReaderService;

@Component
public class WelcomeMessageHandler implements CommonMessageHandler {

    public static final String WELCOME = "welcome";

    @Autowired
    private MessageTemplateContext templateContext;

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
        return new SendMessage()
            .setChatId(message.getChatId())
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
