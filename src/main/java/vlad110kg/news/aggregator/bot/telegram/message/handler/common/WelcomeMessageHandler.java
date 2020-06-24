package vlad110kg.news.aggregator.bot.telegram.message.handler.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;
import vlad110kg.news.aggregator.bot.telegram.message.LangUtils;
import vlad110kg.news.aggregator.bot.telegram.message.template.MessageTemplateContext;
import vlad110kg.news.aggregator.bot.telegram.message.template.TemplateUtils;

@Component
public class WelcomeMessageHandler implements CommonMessageHandler {
    public static final String WELCOME = "welcome";

    @Autowired
    private MessageTemplateContext templateContext;

    @Override
    public BotApiMethod<Message> handle(Message message) {
        //TODO: call reader client
        User from = message.getFrom();
        String lang = LangUtils.getLang(from.getLanguageCode());
        String text = templateContext.processTemplate(WELCOME, lang, TemplateUtils.params("reader_name", from.getFirstName()));
        return new SendMessage()
            .setChatId(message.getChatId())
            .setText(text);
    }

    @Override
    public String trigger() {
        return "/start";
    }
}
