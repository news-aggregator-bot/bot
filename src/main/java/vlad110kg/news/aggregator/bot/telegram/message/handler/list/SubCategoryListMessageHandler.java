package vlad110kg.news.aggregator.bot.telegram.message.handler.list;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import vlad110kg.news.aggregator.bot.telegram.domain.Category;
import vlad110kg.news.aggregator.bot.telegram.domain.ListCategoryResponse;
import vlad110kg.news.aggregator.bot.telegram.message.LangUtils;
import vlad110kg.news.aggregator.bot.telegram.message.MessageUtils;
import vlad110kg.news.aggregator.bot.telegram.message.button.CommandBuilder;
import vlad110kg.news.aggregator.bot.telegram.message.button.MarkupBuilder;
import vlad110kg.news.aggregator.bot.telegram.message.template.MessageTemplateContext;
import vlad110kg.news.aggregator.bot.telegram.message.template.TemplateUtils;
import vlad110kg.news.aggregator.bot.telegram.service.ICategoryService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static vlad110kg.news.aggregator.bot.telegram.message.handler.pick.CategoryPickMessageHandler.CATEGORY;
import static vlad110kg.news.aggregator.bot.telegram.message.template.TemplateUtils.ALL_SUBCATEGORIES;
import static vlad110kg.news.aggregator.bot.telegram.message.template.TemplateUtils.DIR_NEXT;
import static vlad110kg.news.aggregator.bot.telegram.message.template.TemplateUtils.DIR_PREV;

@Component
public class SubCategoryListMessageHandler implements ListMessageHandler {
    public static final String SUBCATEGORY = "subcategory";

    @Autowired
    private CommandBuilder commandBuilder;

    @Autowired
    private MessageTemplateContext templateContext;

    @Autowired
    private ICategoryService categoryService;

    @Override
    public BotApiMethod<Message> handle(Message message, String data) {
        String[] split = MessageUtils.parse(data);
        long parentId = Long.parseLong(split[2]);
        int page = Integer.parseInt(split[3]);
        ListCategoryResponse response = categoryService.list(parentId, page, PAGE_SIZE);
        List<Category> categories = response.getCategories();
        Category parent = categories.get(0);

        MarkupBuilder markup = new MarkupBuilder();
        List<MarkupBuilder.Button> buttons = categories.stream()
            .map(c -> MarkupBuilder.Button.builder().text(c.getLocalised()).command(buildCommand(c)).build())
            .collect(Collectors.toList());

        String lang = LangUtils.getLang(message.getFrom().getLanguageCode());
        String allSubcategoriesText = templateContext.processTemplate(
            ALL_SUBCATEGORIES,
            lang,
            TemplateUtils.params("category", parent.getName())
        );
        markup.addButtons(Arrays.asList(markup.button(allSubcategoriesText, commandBuilder.pick(CATEGORY, parentId))));

        List<MarkupBuilder.Button> navigation = new ArrayList<>();
        if(page > 1) {
            navigation.add(markup.button(templateContext.processTemplate(DIR_PREV, lang), commandBuilder.list(SUBCATEGORY, parentId, page - 1)));
        }

        if (categories.size() == PAGE_SIZE) {
            navigation.add(markup.button(templateContext.processTemplate(DIR_NEXT, lang), commandBuilder.list(SUBCATEGORY, parentId, page + 1)));
        }
        List<List<MarkupBuilder.Button>> partition = Lists.partition(buttons, 3);
        partition.forEach(markup::addButtons);
        markup.addButtons(navigation);

        String listSubcategoryText = templateContext.processTemplate(
            TemplateUtils.LIST_SUBCATEGORY,
            lang,
            TemplateUtils.params("category", parent.getName(), "page", page)
        );
        return new SendMessage()
            .setChatId(message.getChatId())
            .setText(listSubcategoryText)
            .setReplyMarkup(markup.build());
    }

    private String buildCommand(Category c) {
        return commandBuilder.pick(CATEGORY, c.getId());
    }

    @Override
    public String trigger() {
        return SUBCATEGORY;
    }
}
