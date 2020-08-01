package bepicky.bot.client.message.handler.list;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import bepicky.bot.client.domain.Category;
import bepicky.bot.client.message.button.CommandBuilder;
import bepicky.bot.client.message.template.MessageTemplateContext;
import bepicky.bot.client.message.template.TemplateUtils;

import static com.vdurmont.emoji.EmojiParser.parseToUnicode;
import static bepicky.bot.client.message.template.TemplateUtils.DIR_BACK;
import static bepicky.bot.client.message.template.TemplateUtils.DIR_NEXT;
import static bepicky.bot.client.message.template.TemplateUtils.DIR_PREV;
import static bepicky.bot.client.message.template.TemplateUtils.LIST_SUBCATEGORY;
import static bepicky.bot.client.message.template.TemplateUtils.PICK;
import static bepicky.bot.client.message.template.TemplateUtils.name;

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

    protected String prevButtonText(String language) {
        return parseToUnicode(templateContext.processTemplate(DIR_PREV, language));
    }

    protected String nextButtonText(String language) {
        return parseToUnicode(templateContext.processTemplate(DIR_NEXT, language));
    }

    protected String buildText(Category c, String language) {
        if (c.getChildren() == null || c.getChildren().isEmpty()) {
            return parseToUnicode(templateContext.processTemplate(PICK, language, name(c.getLocalised())));
        }
        return parseToUnicode(templateContext.processTemplate(LIST_SUBCATEGORY, language, name(c.getLocalised())));
    }

    protected boolean needsNavigation(int totalAmount) {
        return totalAmount > PAGE_SIZE;
    }

    protected String backButtonText(String language) {
        return parseToUnicode(templateContext.processTemplate(DIR_BACK, language));
    }


}
