package bepicky.bot.client.message.handler;

import bepicky.bot.client.message.LangUtils;
import bepicky.bot.client.message.MessageUtils;
import bepicky.bot.client.message.button.CommandBuilder;
import bepicky.bot.client.message.button.MarkupBuilder;
import bepicky.bot.client.message.template.MessageTemplateContext;
import bepicky.bot.client.message.template.TemplateUtils;
import bepicky.bot.client.service.ICategoryService;
import bepicky.common.domain.dto.CategoryDto;
import bepicky.common.domain.response.CategoryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.ArrayList;
import java.util.List;

import static bepicky.bot.client.message.EntityUtils.CATEGORY;
import static bepicky.bot.client.message.EntityUtils.SUBCATEGORY;

public abstract class AbstractCategoryMessageHandler implements CallbackMessageHandler {

    @Autowired
    protected CommandBuilder commandBuilder;

    @Autowired
    protected MessageTemplateContext templateContext;

    @Autowired
    protected ICategoryService categoryService;

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
        MarkupBuilder markup = new MarkupBuilder();
        String readerLang = response.getReader().getLang();

        List<MarkupBuilder.Button> navigation = new ArrayList<>();
        navigation.add(buildContinueButton(category, readerLang, markup));
        markup.addButtons(navigation);

        String text = templateContext.processTemplate(
            textKey(),
            readerLang,
            TemplateUtils.params(trigger(), category.getName())
        );
        return new HandleResult(text, markup.build());
    }

    protected MarkupBuilder.Button buildContinueButton(CategoryDto category, String lang, MarkupBuilder markup) {
        String buttonText = templateContext.processTemplate(TemplateUtils.DIR_CONTINUE, lang);
        if (category.getParent() == null) {
            return markup.button(buttonText, commandBuilder.list(trigger()));
        }
        return markup.button(buttonText, commandBuilder.list(SUBCATEGORY, category.getParent().getId(), 1));
    }

    @Override
    public String trigger() {
        return CATEGORY;
    }

    protected abstract String textKey();

    protected abstract CategoryResponse handle(Long chatId, Long categoryId);
}
