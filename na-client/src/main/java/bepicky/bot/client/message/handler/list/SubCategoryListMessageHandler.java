package bepicky.bot.client.message.handler.list;

import bepicky.bot.client.message.LangUtils;
import bepicky.bot.client.message.MessageUtils;
import bepicky.bot.client.message.button.MarkupBuilder;
import bepicky.bot.client.message.handler.pick.CategoryPickMessageHandler;
import bepicky.bot.client.message.template.TemplateUtils;
import bepicky.bot.client.service.ICategoryService;
import bepicky.common.domain.response.CategoryResponse;
import bepicky.common.domain.response.ListCategoryResponse;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.vdurmont.emoji.EmojiParser.parseToUnicode;

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

        List<CategoryResponse> categories = response.getList();
        CategoryResponse parent = categories.get(0).getParent();

        MarkupBuilder markup = new MarkupBuilder();
        List<MarkupBuilder.Button> buttons = categories.stream()
            .map(c -> MarkupBuilder.Button.builder()
                .text(buildText(c, response.getLang()))
                .command(buildCommand(c))
                .build())
            .collect(Collectors.toList());

        String allSubcategoriesText = parseToUnicode(templateContext.processTemplate(
            TemplateUtils.ALL_SUBCATEGORIES,
            response.getLang(),
            TemplateUtils.params("category", parent.getName())
        ));

        markup.addButtons(Arrays.asList(markup.button(allSubcategoriesText, commandBuilder.pick(
            CategoryPickMessageHandler.CATEGORY, parentId))));

        List<MarkupBuilder.Button> navigation = new ArrayList<>();
        if (page > 1) {
            String prevText = prevButtonText(response.getLang());
            navigation.add(markup.button(prevText, commandBuilder.list(SUBCATEGORY, parentId, page - 1)));
        }

        navigation.add(markup.button(backButtonText(response.getLang()), buildBackCommand(parent)));

        if (categories.size() == PAGE_SIZE) {
            String nextText = nextButtonText(response.getLang());
            navigation.add(markup.button(nextText, commandBuilder.list(SUBCATEGORY, parentId, page + 1)));
        }

        List<List<MarkupBuilder.Button>> partition = Lists.partition(buttons, 3);
        partition.forEach(markup::addButtons);
        markup.addButtons(navigation);

        String listSubcategoryText = parseToUnicode(templateContext.processTemplate(
            TemplateUtils.LIST_SUBCATEGORIES,
            response.getLang(),
            TemplateUtils.params("category", parent.getName(), "page", page)
        ));
        return new SendMessage()
            .setChatId(message.getChatId())
            .setText(listSubcategoryText)
            .setReplyMarkup(markup.build());
    }

    private String buildBackCommand(CategoryResponse parent) {
        return parent.getParent() == null ? commandBuilder.list(CategoryPickMessageHandler.CATEGORY, parent.getId(), 1)
            : commandBuilder.list(SUBCATEGORY, parent.getParent().getId(), 1);
    }

    private String buildCommand(CategoryResponse c) {
        if (c.getChildren() == null || c.getChildren().isEmpty()) {
            return commandBuilder.pick(CategoryPickMessageHandler.CATEGORY, c.getId());
        }
        return commandBuilder.list(SUBCATEGORY, c.getId(), 1);
    }

    @Override
    public String trigger() {
        return SUBCATEGORY;
    }
}
