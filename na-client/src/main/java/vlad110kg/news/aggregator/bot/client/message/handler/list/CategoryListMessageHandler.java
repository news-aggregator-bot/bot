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
import vlad110kg.news.aggregator.bot.client.service.ICategoryService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.vdurmont.emoji.EmojiParser.parseToUnicode;
import static vlad110kg.news.aggregator.bot.client.message.handler.list.SubCategoryListMessageHandler.SUBCATEGORY;
import static vlad110kg.news.aggregator.bot.client.message.template.TemplateUtils.LIST_CATEGORY;
import static vlad110kg.news.aggregator.bot.client.message.template.TemplateUtils.page;

@Component
public class CategoryListMessageHandler extends AbstractListMessageHandler {

    public static final String CATEGORY = "category";

    @Autowired
    private ICategoryService categoryService;

    @Override
    public BotApiMethod<Message> handle(Message message, String data) {
        String[] split = MessageUtils.parse(data);
        int page = Integer.parseInt(split[2]);
        ListCategoryResponse response = categoryService.list(message.getChatId(), page, PAGE_SIZE);

        if (response.isError()) {
            return error(message.getChatId(), LangUtils.DEFAULT, response.getError().getEntity());
        }

        List<Category> categories = response.getCategories();

        MarkupBuilder markup = new MarkupBuilder();
        List<MarkupBuilder.Button> buttons = categories.stream()
            .map(c -> MarkupBuilder.Button.builder()
                .text(buildText(c, response.getLanguage()))
                .command(buildCommand(c))
                .build())
            .collect(Collectors.toList());

        List<MarkupBuilder.Button> navigation = new ArrayList<>();
        if (needsNavigation(response.getTotalAmount())) {
            if (page > 1) {
                String prevText = prevButtonText(LangUtils.DEFAULT);
                navigation.add(markup.button(prevText, commandBuilder.list(CATEGORY, page - 1)));
            }
        }

        if (needsNavigation(response.getTotalAmount())) {
            if (categories.size() == PAGE_SIZE) {
                String nextText = nextButtonText(LangUtils.DEFAULT);
                navigation.add(markup.button(nextText, commandBuilder.list(CATEGORY, page + 1)));
            }
        }

        List<List<MarkupBuilder.Button>> partition = Lists.partition(buttons, 3);
        partition.forEach(markup::addButtons);
        markup.addButtons(navigation);

        String listCategoryText = parseToUnicode(templateContext.processTemplate(
            LIST_CATEGORY,
            response.getLanguage(),
            page(page)
        ));
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
