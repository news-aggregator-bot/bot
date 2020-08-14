package bepicky.bot.client.message.handler.list;

import bepicky.bot.client.message.LangUtils;
import bepicky.bot.client.message.button.CommandBuilder;
import bepicky.bot.client.message.button.MarkupBuilder;
import bepicky.bot.client.message.template.MessageTemplateContext;
import bepicky.bot.client.message.template.TemplateUtils;
import bepicky.common.domain.dto.CategoryDto;
import bepicky.common.domain.response.AbstractListResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static bepicky.bot.client.message.template.TemplateUtils.DIR_BACK;
import static bepicky.bot.client.message.template.TemplateUtils.DIR_DONE;
import static bepicky.bot.client.message.template.TemplateUtils.DIR_NEXT;
import static bepicky.bot.client.message.template.TemplateUtils.DIR_PREV;
import static bepicky.bot.client.message.template.TemplateUtils.LIST_SUBCATEGORY;
import static bepicky.bot.client.message.template.TemplateUtils.PICK;
import static bepicky.bot.client.message.template.TemplateUtils.name;
import static com.vdurmont.emoji.EmojiParser.parseToUnicode;

@Component
public abstract class AbstractListMessageHandler implements ListMessageHandler {

    @Autowired
    protected CommandBuilder commandBuilder;

    @Autowired
    protected MessageTemplateContext templateContext;

    protected HandleResult error(String msg) {
        String errorMessage = templateContext.errorTemplate(
            LangUtils.DEFAULT,
            TemplateUtils.params(MessageTemplateContext.ERROR, msg)
        );
        return new HandleResult(errorMessage, null);
    }

    protected List<MarkupBuilder.Button> navigation(
        int page,
        String entityKey,
        AbstractListResponse response,
        MarkupBuilder markup
    ) {
        List<MarkupBuilder.Button> navigation = new ArrayList<>();
        if (!response.isFirst()) {
            String prevText = prevButtonText(LangUtils.DEFAULT);
            navigation.add(markup.button(prevText, commandBuilder.list(entityKey, page - 1)));
        }
        navigation.add(markup.done(doneButtonText(response.getReader().getLang())));

        if (!response.isLast()) {
            String nextText = nextButtonText(LangUtils.DEFAULT);
            navigation.add(markup.button(nextText, commandBuilder.list(entityKey, page + 1)));
        }
        return navigation;
    }

    protected String prevButtonText(String language) {
        return parseToUnicode(templateContext.processTemplate(DIR_PREV, language));
    }

    protected String nextButtonText(String language) {
        return parseToUnicode(templateContext.processTemplate(DIR_NEXT, language));
    }

    protected String buildText(CategoryDto c, String language) {
        if (c.getChildren() == null || c.getChildren().isEmpty()) {
            return parseToUnicode(templateContext.processTemplate(PICK, language, name(c.getLocalised())));
        }
        return parseToUnicode(templateContext.processTemplate(LIST_SUBCATEGORY, language, name(c.getLocalised())));
    }

    protected String backButtonText(String language) {
        return parseToUnicode(templateContext.processTemplate(DIR_BACK, language));
    }

    protected String doneButtonText(String language) {
        return parseToUnicode(templateContext.processTemplate(DIR_DONE, language));
    }

}
