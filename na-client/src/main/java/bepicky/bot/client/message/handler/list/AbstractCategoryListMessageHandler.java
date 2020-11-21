package bepicky.bot.client.message.handler.list;

import bepicky.bot.client.message.MessageUtils;
import bepicky.bot.client.message.button.CommandType;
import bepicky.bot.client.message.button.MarkupBuilder;
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

import static bepicky.bot.client.message.button.CommandType.LIST;
import static bepicky.bot.client.message.button.CommandType.SUBLIST;
import static bepicky.bot.client.message.template.TemplateUtils.page;
import static com.vdurmont.emoji.EmojiParser.parseToUnicode;

@Component
public abstract class AbstractCategoryListMessageHandler extends AbstractListMessageHandler {

    public static final int FOUR_PAGE_SIZE = 4;

    @Autowired
    protected ICategoryService categoryService;

    private final Map<CommandType, CommandType> sublistMapping = ImmutableMap.<CommandType, CommandType>builder()
        .put(LIST, SUBLIST)
        .build();

    @Override
    public HandleResult handle(Message message, String data) {
        String[] split = MessageUtils.parse(data);
        int page = Integer.parseInt(split[2]);
        CategoryListResponse response = getCategories(message.getChatId(), page);

        if (response.isError()) {
            return error(response.getError().getEntity());
        }

        String readerLang = response.getReader().getLang();
        List<CategoryDto> categories = response.getList();

        MarkupBuilder markup = new MarkupBuilder();
        List<MarkupBuilder.Button> buttons = categories.stream()
            .map(c -> MarkupBuilder.Button.builder()
                .text(buildText(c, readerLang))
                .command(buildCommand(c))
                .build())
            .collect(Collectors.toList());

        List<MarkupBuilder.Button> navigation = navigation(page, response, markup);
        List<List<MarkupBuilder.Button>> partition = Lists.partition(buttons, 2);
        partition.forEach(markup::addButtons);
        markup.addButtons(navigation);

        String listCategoryText = parseToUnicode(templateContext.processTemplate(
            msgTextKey(),
            readerLang,
            page(page)
        ));
        flowContext.updateFlow(
            response.getReader().getChatId(),
            msgTextKey(), entityType(), commandType()
        );
        return new HandleResult(listCategoryText, markup.build());
    }

    private String buildCommand(CategoryDto c) {
        if (c.getChildren() == null || c.getChildren().isEmpty()) {
            return c.isPicked() ?
                commandBuilder.remove(trigger(), c.getId()) :
                commandBuilder.pick(trigger(), c.getId());
        }

        CommandType sublistCommand = sublistMapping.getOrDefault(commandType(), commandType());
        return commandBuilder.sublist(sublistCommand, trigger(), c.getId(), 1);
    }

    protected abstract CategoryListResponse getCategories(long chatId, int page);

    protected abstract String msgTextKey();

}
