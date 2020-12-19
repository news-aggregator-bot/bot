package bepicky.bot.client.message.handler.list;

import bepicky.bot.client.message.LangUtils;
import bepicky.bot.client.message.button.InlineMarkupBuilder;
import bepicky.bot.client.message.command.CommandManager;
import bepicky.bot.client.message.handler.context.ChatChain;
import bepicky.bot.client.message.handler.context.ChatChainLink;
import bepicky.bot.client.message.handler.context.ChatChainManager;
import bepicky.bot.client.message.template.MessageTemplateContext;
import bepicky.bot.client.message.template.TemplateUtils;
import bepicky.common.domain.dto.CategoryDto;
import bepicky.common.domain.dto.ReaderDto;
import bepicky.common.domain.response.AbstractListResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static bepicky.bot.client.message.template.ButtonNames.DIR_BACK;
import static bepicky.bot.client.message.template.ButtonNames.DIR_DONE;
import static bepicky.bot.client.message.template.ButtonNames.DIR_NEXT;
import static bepicky.bot.client.message.template.ButtonNames.DIR_PREV;
import static bepicky.bot.client.message.template.ButtonNames.PICK;
import static bepicky.bot.client.message.template.ButtonNames.REMOVE;
import static bepicky.bot.client.message.template.TemplateUtils.LIST_SUBCATEGORY;
import static bepicky.bot.client.message.template.TemplateUtils.name;
import static com.vdurmont.emoji.EmojiParser.parseToUnicode;

@Component
public abstract class AbstractListMessageHandler implements ListMessageHandler {

    @Autowired
    protected CommandManager cmdMngr;

    @Autowired
    protected MessageTemplateContext templateContext;

    @Autowired
    protected ChatChainManager chainManager;

    protected HandleResult error(String msg) {
        String errorMessage = templateContext.errorTemplate(
            LangUtils.DEFAULT,
            TemplateUtils.params(MessageTemplateContext.ERROR, msg)
        );
        return new HandleResult(errorMessage, null);
    }

    protected List<InlineMarkupBuilder.InlineButton> pagination(
        int page,
        AbstractListResponse response,
        InlineMarkupBuilder markup
    ) {
        List<InlineMarkupBuilder.InlineButton> pagination = new ArrayList<>();
        if (!response.isFirst()) {
            String prevText = prevButtonText();
            pagination.add(markup.button(prevText, cmdMngr.list(entityType(), page - 1)));
        }
        if (!response.isLast()) {
            String nextText = nextButtonText();
            pagination.add(markup.button(nextText, cmdMngr.list(entityType(), page + 1)));
        }
        return pagination;
    }

    protected List<InlineMarkupBuilder.InlineButton> navigation(InlineMarkupBuilder markup, ReaderDto reader) {
        List<InlineMarkupBuilder.InlineButton> navigation = new ArrayList<>();
        ChatChain currentChain = chainManager.getChain(reader.getChatId());
        ChatChainLink previous = currentChain.previous();
        if (previous != null) {
            navigation.add(markup.button(
                backButtonText(reader.getLang()),
                cmdMngr.goPrevious(),
                previous.getCommand()
            ));
        }
        ChatChainLink next = chainManager.next(reader.getChatId());
        navigation.add(markup.button(doneButtonText(reader.getLang()), cmdMngr.goNext(), next.getCommand()));
        return navigation;
    }

    protected String prevButtonText() {
        return templateContext.processEmojiTemplate(DIR_PREV, LangUtils.ALL);
    }

    protected String nextButtonText() {
        return templateContext.processEmojiTemplate(DIR_NEXT, LangUtils.ALL);
    }

    protected String buildText(CategoryDto c, String language) {
        if (c.getChildren() == null || c.getChildren().isEmpty()) {
            String buttonKey = c.isPicked() ? REMOVE : PICK;
            return parseToUnicode(templateContext.processTemplate(buttonKey, language, name(c.getLocalised())));
        }
        return parseToUnicode(templateContext.processTemplate(LIST_SUBCATEGORY, language, name(c.getLocalised())));
    }

    protected String backButtonText(String language) {
        return templateContext.processEmojiTemplate(DIR_BACK, language);
    }

    protected String doneButtonText(String language) {
        return templateContext.processEmojiTemplate(DIR_DONE, language);
    }

}
