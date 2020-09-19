package bepicky.bot.client.message.handler.list;

import bepicky.bot.client.message.EntityType;
import bepicky.bot.client.message.MessageUtils;
import bepicky.bot.client.message.button.MarkupBuilder;
import bepicky.bot.client.message.handler.context.ChatFlow;
import bepicky.bot.client.message.handler.context.ChatFlowContext;
import bepicky.bot.client.service.ICategoryService;
import bepicky.common.domain.dto.CategoryDto;
import bepicky.common.domain.response.CategoryListResponse;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static bepicky.bot.client.message.template.TemplateUtils.LIST_CATEGORY;
import static bepicky.bot.client.message.template.TemplateUtils.LIST_REGIONS;
import static bepicky.bot.client.message.template.TemplateUtils.page;
import static com.vdurmont.emoji.EmojiParser.parseToUnicode;

@Component
public class CategoryListMessageHandler extends AbstractListMessageHandler {

    @Autowired
    private ICategoryService categoryService;

    @Autowired
    private ChatFlowContext flowContext;

    private final Map<EntityType, String> typeContainer =
        ImmutableMap.<EntityType, String>builder()
            .put(EntityType.CATEGORY, "COMMON")
            .put(EntityType.REGION, "REGION")
            .build();

    private final Map<EntityType, String> listKeysContainer =
        ImmutableMap.<EntityType, String>builder()
            .put(EntityType.CATEGORY, LIST_CATEGORY)
            .put(EntityType.REGION, LIST_REGIONS)
            .build();

    @Override
    public HandleResult handle(Message message, String data) {
        String[] split = MessageUtils.parse(data);
        int page = Integer.parseInt(split[2]);
        ChatFlow current = flowContext.updateCategory(message.getChatId());
        CategoryListResponse response = categoryService.list(
            message.getChatId(),
            typeContainer.getOrDefault(current.getType(), typeContainer.get(EntityType.CATEGORY)),
            page,
            PAGE_SIZE
        );

        if (response.isError()) {
            return error(response.getError().getEntity());
        }

        String readerLang = response.getReader().getLang();
        List<CategoryDto> categories = response.getList();
        flowContext.updateCategory(response.getReader().getChatId());

        MarkupBuilder markup = new MarkupBuilder();
        List<MarkupBuilder.Button> buttons = categories.stream()
            .map(c -> MarkupBuilder.Button.builder()
                .text(buildText(c, readerLang))
                .command(buildCommand(c))
                .build())
            .collect(Collectors.toList());

        List<MarkupBuilder.Button> navigation = navigation(page, trigger(), response, markup);
        List<List<MarkupBuilder.Button>> partition = Lists.partition(buttons, 3);
        partition.forEach(markup::addButtons);
        markup.addButtons(navigation);

        String listCategoryText = parseToUnicode(templateContext.processTemplate(
            listKeysContainer.getOrDefault(current.getType(), listKeysContainer.get(EntityType.CATEGORY)),
            readerLang,
            page(page)
        ));
        return new HandleResult(listCategoryText, markup.build());
    }


    private String buildCommand(CategoryDto c) {
        if (c.getChildren() == null || c.getChildren().isEmpty()) {
            return commandBuilder.pick(trigger(), c.getId());
        }
        return commandBuilder.list(EntityType.SUBCATEGORY.lower(), c.getId(), 1);
    }

    @Override
    public String trigger() {
        return EntityType.CATEGORY.lower();
    }
}
