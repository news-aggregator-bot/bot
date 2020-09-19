package bepicky.bot.client.message.handler.list;

import bepicky.bot.client.message.EntityType;
import bepicky.bot.client.message.MessageUtils;
import bepicky.bot.client.message.button.MarkupBuilder;
import bepicky.bot.client.message.template.TemplateUtils;
import bepicky.bot.client.service.ICategoryService;
import bepicky.common.domain.dto.CategoryDto;
import bepicky.common.domain.response.CategoryListResponse;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.vdurmont.emoji.EmojiParser.parseToUnicode;

@Component
public class SubCategoryListMessageHandler extends AbstractListMessageHandler {

    @Autowired
    private ICategoryService categoryService;

    @Override
    public HandleResult handle(Message message, String data) {
        String[] split = MessageUtils.parse(data);
        long parentId = Long.parseLong(split[2]);
        int page = Integer.parseInt(split[3]);

        CategoryListResponse response = categoryService.list(message.getChatId(), parentId, page, PAGE_SIZE);
        if (response.isError()) {
            return error(response.getError().getEntity());
        }
        List<CategoryDto> categories = response.getList();
        CategoryDto parent = categories.get(0).getParent();

        MarkupBuilder markup = new MarkupBuilder();
        String readerLang = response.getReader().getLang();
        List<MarkupBuilder.Button> buttons = categories.stream()
            .map(c -> MarkupBuilder.Button.builder()
                .text(buildText(c, readerLang))
                .command(buildCommand(c))
                .build())
            .collect(Collectors.toList());

        String allSubcategoriesText = parseToUnicode(templateContext.processTemplate(
            TemplateUtils.ALL_SUBCATEGORIES,
            readerLang,
            TemplateUtils.params("category", parent.getName())
        ));

        markup.addButtons(Arrays.asList(markup.button(
            allSubcategoriesText,
            commandBuilder.pick(EntityType.CATEGORY.lower(), parentId)
        )));


        List<MarkupBuilder.Button> navigation = new ArrayList<>();
        if (!response.isFirst()) {
            String prevText = prevButtonText(readerLang);
            navigation.add(markup.button(prevText, commandBuilder.list(trigger(), parentId, page - 1)));
        }
        navigation.add(markup.button(backButtonText(readerLang), buildBackCommand(parent)));
        if (!response.isLast()) {
            String nextText = nextButtonText(readerLang);
            navigation.add(markup.button(nextText, commandBuilder.list(trigger(), parentId, page + 1)));
        }

        List<List<MarkupBuilder.Button>> partition = Lists.partition(buttons, 3);
        partition.forEach(markup::addButtons);
        markup.addButtons(navigation);

        String listSubcategoryText = parseToUnicode(templateContext.processTemplate(
            TemplateUtils.LIST_SUBCATEGORIES,
            readerLang,
            TemplateUtils.params("category", parent.getName(), "page", page)
        ));
        return new HandleResult(listSubcategoryText, markup.build());
    }

    private String buildBackCommand(CategoryDto parent) {
        return parent.getParent() == null ? commandBuilder.list(EntityType.CATEGORY.lower(), 1)
            : commandBuilder.list(trigger(), parent.getParent().getId(), 1);
    }

    private String buildCommand(CategoryDto c) {
        if (c.getChildren() == null || c.getChildren().isEmpty()) {
            return commandBuilder.pick(EntityType.CATEGORY.lower(), c.getId());
        }
        return commandBuilder.list(trigger(), c.getId(), 1);
    }

    @Override
    public String trigger() {
        return EntityType.SUBCATEGORY.lower();
    }
}
