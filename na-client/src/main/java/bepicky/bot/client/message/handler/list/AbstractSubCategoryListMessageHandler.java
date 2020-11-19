package bepicky.bot.client.message.handler.list;

import bepicky.bot.client.message.MessageUtils;
import bepicky.bot.client.message.button.CommandType;
import bepicky.bot.client.message.button.MarkupBuilder;
import bepicky.bot.client.message.template.TemplateUtils;
import bepicky.bot.client.service.ICategoryService;
import bepicky.common.domain.dto.CategoryDto;
import bepicky.common.domain.response.CategoryListResponse;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.objects.Message;
import reactor.util.function.Tuple2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static bepicky.bot.client.message.template.TemplateUtils.PICK_ALL_SUBCATEGORIES;
import static bepicky.bot.client.message.template.TemplateUtils.REMOVE_ALL_SUBCATEGORIES;
import static com.vdurmont.emoji.EmojiParser.parseToUnicode;

public abstract class AbstractSubCategoryListMessageHandler extends AbstractListMessageHandler {

    @Autowired
    protected ICategoryService categoryService;

    private final Map<CommandType, CommandType> parentCommandMapping =
        ImmutableMap.<CommandType, CommandType>builder()
            .put(CommandType.SUBLIST, CommandType.LIST)
            .put(CommandType.SUBLIST_PICKED, CommandType.LIST_PICKED)
            .put(CommandType.SUBLIST_NOT_PICKED, CommandType.LIST_NOT_PICKED)
            .build();

    @Override
    public HandleResult handle(Message message, String data) {
        String[] split = MessageUtils.parse(data);
        long parentId = Long.parseLong(split[2]);
        int page = Integer.parseInt(split[3]);

        CategoryListResponse response = getSubCategories(message.getChatId(), parentId, page);
        if (response.isError()) {
            return error(response.getError().getEntity());
        }
        List<CategoryDto> categories = response.getList();
        CategoryDto parent = categories.get(0).getParent();

        MarkupBuilder markup = new MarkupBuilder();
        String readerLang = response.getReader().getLang();
        List<MarkupBuilder.Button> subcategoryButtons = categories.stream()
            .map(c -> MarkupBuilder.Button.builder()
                .text(buildText(c, readerLang))
                .command(buildCommand(c))
                .build())
            .collect(Collectors.toList());

        MarkupBuilder.Button allSubCategoriesBtn = buildAllSubCategoryButton(
            parentId,
            parent,
            markup,
            readerLang
        );
        markup.addButtons(Arrays.asList(allSubCategoriesBtn));

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

        List<List<MarkupBuilder.Button>> partition = Lists.partition(subcategoryButtons, 2);
        partition.forEach(markup::addButtons);
        markup.addButtons(navigation);

        Tuple2<String, Map<String, Object>> textData = msgTextData(parent, page);
        String listSubcategoryText = parseToUnicode(templateContext.processTemplate(
            textData.getT1(),
            readerLang,
            textData.getT2()
        ));
        flowContext.updateFlow(
            response.getReader().getChatId(),
            textData.getT1(), entityType(), commandType()
        );
        return new HandleResult(listSubcategoryText, markup.build());
    }

    private MarkupBuilder.Button buildAllSubCategoryButton(
        long parentId,
        CategoryDto parent,
        MarkupBuilder markup,
        String readerLang
    ) {
        String allSubcategoriesText = parseToUnicode(templateContext.processTemplate(
            parent.isPicked() ? REMOVE_ALL_SUBCATEGORIES : PICK_ALL_SUBCATEGORIES,
            readerLang,
            TemplateUtils.params("category", parent.getName())
        ));

        String parentCommand = parent.isPicked() ?
            commandBuilder.remove(trigger(), parentId) :
            commandBuilder.pick(trigger(), parentId);
        return markup.button(allSubcategoriesText, parentCommand);
    }

    protected abstract CategoryListResponse getSubCategories(long chatId, long parentId, int page);

    protected abstract Tuple2<String, Map<String, Object>> msgTextData(CategoryDto parent, int page);

    private String buildBackCommand(CategoryDto parent) {
        return parent.getParent() == null ? commandBuilder.list(parentCommandMapping.get(commandType()), trigger())
            : commandBuilder.sublist(commandType(), trigger(), parent.getParent().getId());
    }

    private String buildCommand(CategoryDto c) {
        if (c.getChildren() == null || c.getChildren().isEmpty()) {
            return c.isPicked() ?
                commandBuilder.remove(trigger(), c.getId()) :
                commandBuilder.pick(trigger(), c.getId());
        }
        return commandBuilder.sublist(commandType(), trigger(), c.getId(), 1);
    }
}
