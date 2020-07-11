package vlad110kg.news.aggregator.bot.client.message.handler.list;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import vlad110kg.news.aggregator.bot.client.domain.Category;
import vlad110kg.news.aggregator.bot.client.domain.response.ListCategoryResponse;
import vlad110kg.news.aggregator.bot.client.message.LangUtils;
import vlad110kg.news.aggregator.bot.client.message.MessageUtils;
import vlad110kg.news.aggregator.bot.client.message.button.MarkupBuilder;
import vlad110kg.news.aggregator.bot.client.message.template.TemplateUtils;
import vlad110kg.news.aggregator.bot.client.service.ICategoryService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.vdurmont.emoji.EmojiParser.parseToUnicode;
import static vlad110kg.news.aggregator.bot.client.message.handler.pick.CategoryPickMessageHandler.CATEGORY;
import static vlad110kg.news.aggregator.bot.client.message.template.TemplateUtils.ALL_SUBCATEGORIES;

@Component
public class SubCategoryListMessageHandler extends AbstractListMessageHandler {

    public static final String SUBCATEGORY = "subcategory";

    @Autowired
    private ICategoryService categoryService;

    @Override
    public BotApiMethod<Message> handle(Message message, String data) {
        String[] split = MessageUtils.parse(data);
        long parentId = Long.parseLong(split[2]);
        int page = Integer.parseInt(split[3]);

        ListCategoryResponse response = categoryService.list(message.getChatId(), parentId, page, PAGE_SIZE);
        if (response.isError()) {
            return error(message.getChatId(), LangUtils.DEFAULT, response.getError().getEntity());
        }

        List<Category> categories = response.getCategories();
        Category parent = categories.get(0).getParent();

        MarkupBuilder markup = new MarkupBuilder();
        List<MarkupBuilder.Button> buttons = categories.stream()
            .map(c -> MarkupBuilder.Button.builder()
                .text(buildText(c, response.getLanguage()))
                .command(buildCommand(c))
                .build())
            .collect(Collectors.toList());

        String allSubcategoriesText = parseToUnicode(templateContext.processTemplate(
            ALL_SUBCATEGORIES,
            response.getLanguage(),
            TemplateUtils.params("category", parent.getName())
        ));

        markup.addButtons(Arrays.asList(markup.button(allSubcategoriesText, commandBuilder.pick(CATEGORY, parentId))));

        List<MarkupBuilder.Button> navigation = new ArrayList<>();
        if (page > 1) {
            String prevText = prevButtonText(response.getLanguage());
            navigation.add(markup.button(prevText, commandBuilder.list(SUBCATEGORY, parentId, page - 1)));
        }

        navigation.add(markup.button(backButtonText(response.getLanguage()), buildBackCommand(parent)));

        if (categories.size() == PAGE_SIZE) {
            String nextText = nextButtonText(response.getLanguage());
            navigation.add(markup.button(nextText, commandBuilder.list(SUBCATEGORY, parentId, page + 1)));
        }

        List<List<MarkupBuilder.Button>> partition = Lists.partition(buttons, 3);
        partition.forEach(markup::addButtons);
        markup.addButtons(navigation);

        String listSubcategoryText = parseToUnicode(templateContext.processTemplate(
            TemplateUtils.LIST_SUBCATEGORIES,
            response.getLanguage(),
            TemplateUtils.params("category", parent.getName(), "page", page)
        ));
        return new SendMessage()
            .setChatId(message.getChatId())
            .setText(listSubcategoryText)
            .setReplyMarkup(markup.build());
    }

    private String buildBackCommand(Category parent) {
        return parent.getParent() == null ? commandBuilder.list(CATEGORY, parent.getId(), 1)
            : commandBuilder.list(SUBCATEGORY, parent.getParent().getId(), 1);
    }

    private String buildCommand(Category c) {
        if (c.getChildren() == null || c.getChildren().isEmpty()) {
            return commandBuilder.pick(CATEGORY, c.getId());
        }
        return commandBuilder.list(SUBCATEGORY, c.getId(), 1);
    }

    @Override
    public String trigger() {
        return SUBCATEGORY;
    }
}
