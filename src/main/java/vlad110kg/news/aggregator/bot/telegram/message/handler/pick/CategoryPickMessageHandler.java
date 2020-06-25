package vlad110kg.news.aggregator.bot.telegram.message.handler.pick;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import vlad110kg.news.aggregator.bot.telegram.domain.Category;
import vlad110kg.news.aggregator.bot.telegram.message.LangUtils;
import vlad110kg.news.aggregator.bot.telegram.message.MessageUtils;
import vlad110kg.news.aggregator.bot.telegram.message.button.CommandBuilder;
import vlad110kg.news.aggregator.bot.telegram.message.button.MarkupBuilder;
import vlad110kg.news.aggregator.bot.telegram.message.template.MessageTemplateContext;
import vlad110kg.news.aggregator.bot.telegram.message.template.TemplateUtils;
import vlad110kg.news.aggregator.bot.telegram.service.ICategoryService;

import java.util.ArrayList;
import java.util.List;

import static vlad110kg.news.aggregator.bot.telegram.message.handler.list.SubCategoryListMessageHandler.SUBCATEGORY;
import static vlad110kg.news.aggregator.bot.telegram.message.template.TemplateUtils.DIR_CONTINUE;

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
        Category category = categoryService.subscribe(message.getChatId(), categoryId);

        MarkupBuilder markup = new MarkupBuilder();

        List<MarkupBuilder.Button> navigation = new ArrayList<>();
        navigation.add(buildContinueButton(category, message, markup));
        markup.addButtons(navigation);

        String lang = LangUtils.getLang(message.getFrom().getLanguageCode());
        String categoryPickText = templateContext.processTemplate(
            TemplateUtils.PICK_CATEGORY,
            lang,
            TemplateUtils.params("category", category.getName())
        );
        return new SendMessage()
            .setChatId(message.getChatId())
            .setText(categoryPickText)
            .setReplyMarkup(markup.build());
    }

    private MarkupBuilder.Button buildContinueButton(Category category, Message message, MarkupBuilder markup) {
        String lang = LangUtils.getLang(message.getFrom().getLanguageCode());
        String buttonText = templateContext.processTemplate(DIR_CONTINUE, lang);
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
