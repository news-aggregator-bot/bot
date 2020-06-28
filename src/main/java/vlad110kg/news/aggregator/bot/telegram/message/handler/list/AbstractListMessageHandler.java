package vlad110kg.news.aggregator.bot.telegram.message.handler.list;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import vlad110kg.news.aggregator.bot.telegram.domain.Category;
import vlad110kg.news.aggregator.bot.telegram.message.button.CommandBuilder;
import vlad110kg.news.aggregator.bot.telegram.message.template.MessageTemplateContext;
import vlad110kg.news.aggregator.bot.telegram.message.template.TemplateUtils;

import static vlad110kg.news.aggregator.bot.telegram.message.template.TemplateUtils.LIST_SUBCATEGORY;
import static vlad110kg.news.aggregator.bot.telegram.message.template.TemplateUtils.PICK_CATEGORY;
import static vlad110kg.news.aggregator.bot.telegram.message.template.TemplateUtils.name;

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

    protected String buildText(Category c, String language) {
        if (c.getChildren() == null || c.getChildren().isEmpty()) {
            return templateContext.processTemplate(LIST_SUBCATEGORY, language, name(c.getLocalised()));
        }
        return templateContext.processTemplate(PICK_CATEGORY, language, name(c.getLocalised()));
    }

}
