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
import vlad110kg.news.aggregator.bot.telegram.service.ICategoryService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static vlad110kg.news.aggregator.bot.telegram.message.handler.list.SubCategoryListMessageHandler.SUBCATEGORY;
import static vlad110kg.news.aggregator.bot.telegram.message.template.TemplateUtils.DIR_NEXT;
import static vlad110kg.news.aggregator.bot.telegram.message.template.TemplateUtils.DIR_PREV;
import static vlad110kg.news.aggregator.bot.telegram.message.template.TemplateUtils.LIST_CATEGORY;
import static vlad110kg.news.aggregator.bot.telegram.message.template.TemplateUtils.params;

@Component
public class CategoryListMessageHandler implements ListMessageHandler {
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
        int page = Integer.parseInt(split[2]);
        ListCategoryResponse list = categoryService.list(page, PAGE_SIZE);
        List<Category> categories = list.getCategories();

        MarkupBuilder markup = new MarkupBuilder();
        List<MarkupBuilder.Button> buttons = categories.stream()
            .map(c -> MarkupBuilder.Button.builder().text(c.getLocalised()).command(buildCommand(c)).build())
            .collect(Collectors.toList());

        String lang = LangUtils.getLang(message.getFrom().getLanguageCode());
        List<MarkupBuilder.Button> navigation = new ArrayList<>();
        if(page > 1) {
            navigation.add(markup.button(templateContext.processTemplate(DIR_PREV, lang), commandBuilder.list(CATEGORY, page - 1)));
        }

        if (categories.size() == PAGE_SIZE) {
            navigation.add(markup.button(templateContext.processTemplate(DIR_NEXT, lang), commandBuilder.list(CATEGORY, page + 1)));
        }

        List<List<MarkupBuilder.Button>> partition = Lists.partition(buttons, 3);
        partition.forEach(markup::addButtons);
        markup.addButtons(navigation);

        String listCategoryText = templateContext.processTemplate(LIST_CATEGORY, lang, params("page", page));
        return new SendMessage()
            .setChatId(message.getChatId())
            .setText(listCategoryText)
            .setReplyMarkup(markup.build());
    }

    private String buildCommand(Category c) {
        if (c.getChildren() == null || c.getChildren().isEmpty()) {
            return commandBuilder.pick(CATEGORY, c.getId());
        }
        return commandBuilder.list(SUBCATEGORY, c.getId(), 1);
    }

    @Override
    public String trigger() {
        return CATEGORY;
    }
}
