package bepicky.bot.client.message.handler;

import bepicky.bot.client.message.LangUtils;
import bepicky.bot.client.message.MessageUtils;
import bepicky.bot.client.message.button.CommandBuilder;
import bepicky.bot.client.message.button.InlineMarkupBuilder;
import bepicky.bot.client.message.handler.context.ChatFlow;
import bepicky.bot.client.message.handler.context.ChatFlowManager;
import bepicky.bot.client.message.template.ButtonNames;
import bepicky.bot.client.message.template.MessageTemplateContext;
import bepicky.bot.client.message.template.TemplateUtils;
import bepicky.bot.client.service.ICategoryService;
import bepicky.common.domain.dto.CategoryDto;
import bepicky.common.domain.response.CategoryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractCategoryMessageHandler implements CallbackMessageHandler {

    @Autowired
    protected CommandBuilder commandBuilder;

    @Autowired
    protected MessageTemplateContext templateContext;

    @Autowired
    protected ICategoryService categoryService;

    @Autowired
    protected ChatFlowManager flowContext;

    @Override
    public HandleResult handle(Message message, String data) {
        String[] split = MessageUtils.parse(data);
        long categoryId = Long.parseLong(split[2]);
        CategoryResponse response = handle(message.getChatId(), categoryId);
        if (response.isError()) {
            String errorText = templateContext.errorTemplate(LangUtils.DEFAULT);
            // handling
            return new HandleResult(errorText, null);
        }

        CategoryDto category = response.getCategory();
        InlineMarkupBuilder markup = new InlineMarkupBuilder();
        String readerLang = response.getReader().getLang();
        ChatFlow current = flowContext.current(message.getChatId());

        List<InlineMarkupBuilder.InlineButton> navigation = new ArrayList<>();
        navigation.add(buildContinueButton(category, current, readerLang, markup));
        markup.addButtons(navigation);

        String text = templateContext.processTemplate(
            textKey(),
            readerLang,
            TemplateUtils.name(category.getName())
        );
        return new HandleResult(text, markup.build());
    }

    protected InlineMarkupBuilder.InlineButton buildContinueButton(
        CategoryDto category,
        ChatFlow flow,
        String lang,
        InlineMarkupBuilder markup
    ) {
        String buttonText = templateContext.processEmojiTemplate(ButtonNames.DIR_CONTINUE, lang);
        if (category.getParent() == null) {
            return markup.button(buttonText, commandBuilder.list(flow.getCommandType(), trigger()));
        }
        return markup.button(
            buttonText,
            commandBuilder.sublist(flow.getCommandType(), trigger(), category.getParent().getId())
        );
    }

    protected abstract String textKey();

    protected abstract CategoryResponse handle(Long chatId, Long categoryId);
}
