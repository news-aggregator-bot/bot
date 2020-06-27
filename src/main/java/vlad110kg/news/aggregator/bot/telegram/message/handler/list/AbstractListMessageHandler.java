package vlad110kg.news.aggregator.bot.telegram.message.handler.list;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import vlad110kg.news.aggregator.bot.telegram.message.button.CommandBuilder;
import vlad110kg.news.aggregator.bot.telegram.message.template.MessageTemplateContext;
import vlad110kg.news.aggregator.bot.telegram.message.template.TemplateUtils;

@Component
public abstract class AbstractListMessageHandler implements ListMessageHandler {

    @Autowired
    protected CommandBuilder commandBuilder;

    @Autowired
    protected MessageTemplateContext templateContext;

    protected BotApiMethod<Message> error(long chatId, String lang, String msg) {
        String errorMessage = templateContext.errorTemplate(
            lang,
            TemplateUtils.params(MessageTemplateContext.ERROR, msg)
        );
        return new SendMessage()
            .setChatId(chatId)
            .setText(errorMessage);
    }

}
