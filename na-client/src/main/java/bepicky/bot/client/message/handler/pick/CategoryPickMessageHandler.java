package bepicky.bot.client.message.handler.pick;

import bepicky.bot.client.domain.Category;
import bepicky.bot.client.message.template.TemplateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import bepicky.bot.client.domain.response.PickCategoryResponse;
import bepicky.bot.client.message.LangUtils;
import bepicky.bot.client.message.MessageUtils;
import bepicky.bot.client.message.button.CommandBuilder;
import bepicky.bot.client.message.button.MarkupBuilder;
import bepicky.bot.client.message.template.MessageTemplateContext;
import bepicky.bot.client.service.ICategoryService;

import java.util.ArrayList;
import java.util.List;

import static bepicky.bot.client.message.handler.list.SubCategoryListMessageHandler.SUBCATEGORY;

@Component
public class CategoryPickMessageHandler implements PickMessageHandler {
    public static final String CATEGORY = "category";

    @Autowired
    private CommandBuilder commandBuilder;

    @Autowired
    private MessageTemplateContext templateContext;

    @Autowired
    private ICategoryService categoryService;

    @Override
    public BotApiMethod<Message> handle(Message message, String data) {
        String[] split = MessageUtils.parse(data);
        long categoryId = Long.parseLong(split[2]);
        PickCategoryResponse response = categoryService.pick(message.getChatId(), categoryId);
        if (response.isError()) {
            String errorText = templateContext.errorTemplate(LangUtils.DEFAULT);// TODO: 28.06.2020 add normal error
            // handling
            return new SendMessage().setChatId(message.getChatId()).setText(errorText);
        }

        Category category = response.getCategory();
        MarkupBuilder markup = new MarkupBuilder();

        List<MarkupBuilder.Button> navigation = new ArrayList<>();
        navigation.add(buildContinueButton(category, response.getLanguage(), markup));
        markup.addButtons(navigation);

        String categoryPickText = templateContext.processTemplate(
            TemplateUtils.PICK_CATEGORY_SUCCESS,
            response.getLanguage(),
            TemplateUtils.params("category", category.getName())
        );
        return new SendMessage()
            .setChatId(message.getChatId())
            .setText(categoryPickText)
            .setReplyMarkup(markup.build());
    }

    private MarkupBuilder.Button buildContinueButton(Category category, String lang, MarkupBuilder markup) {
        String buttonText = templateContext.processTemplate(TemplateUtils.DIR_CONTINUE, lang);
        if (category.getParent() == null) {
            return markup.button(buttonText, commandBuilder.list(CATEGORY));
        }
        return markup.button(buttonText, commandBuilder.list(SUBCATEGORY, category.getParent().getId(), 1));
    }

    @Override
    public String trigger() {
        return CATEGORY;
    }
}
