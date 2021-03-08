package bepicky.bot.client.message.handler.list;

import bepicky.bot.core.message.button.InlineMarkupBuilder;
import bepicky.bot.client.service.ICategoryService;
import bepicky.bot.core.cmd.ChatCommand;
import bepicky.common.domain.dto.CategoryDto;
import bepicky.common.domain.response.CategoryListResponse;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static bepicky.bot.core.message.template.TemplateUtils.page;
import static com.vdurmont.emoji.EmojiParser.parseToUnicode;

@Component
public abstract class AbstractCategoryListMessageHandler extends AbstractListMessageHandler {

    @Autowired
    protected ICategoryService categoryService;

    @Override
    public HandleResult handle(ChatCommand cc) {
        int page = cc.getPage();
        CategoryListResponse response = getCategories(cc.getChatId(), page);

        if (response.isError()) {
            return error(response.getError().getEntity());
        }

        String readerLang = response.getReader().getLang();
        List<CategoryDto> categories = response.getList();

        InlineMarkupBuilder markup = new InlineMarkupBuilder();
        List<InlineMarkupBuilder.InlineButton> buttons = categories.stream()
            .map(c -> new InlineMarkupBuilder.InlineButton(buildText(c, readerLang), buildCommand(c, page)))
            .collect(Collectors.toList());

        List<InlineMarkupBuilder.InlineButton> pagination = pagination(page, response, markup);
        List<InlineMarkupBuilder.InlineButton> navigation = navigation(markup, response.getReader());
        List<List<InlineMarkupBuilder.InlineButton>> partition = Lists.partition(buttons, 2);
        partition.forEach(markup::addButtons);
        markup.addButtons(pagination);
        markup.addButtons(navigation);

        String listCategoryText = parseToUnicode(templateContext.processTemplate(
            msgTextKey(),
            readerLang,
            page(page)
        ));
        chainManager.updatePage(cc.getChatId(), page);
        return new HandleResult(listCategoryText, markup.build());
    }

    private List<String> buildCommand(CategoryDto c, int page) {
        if (c.getChildren() == null || c.getChildren().isEmpty()) {
            String categoryCmd = c.isPicked() ?
                cmdMngr.remove(entityType(), c.getId()) :
                cmdMngr.pick(entityType(), c.getId());
            return Arrays.asList(categoryCmd, cmdMngr.list(entityType(), page));
        }

        return Arrays.asList(cmdMngr.sublist(entityType(), c.getId(), 1));
    }

    protected abstract CategoryListResponse getCategories(long chatId, int page);

    protected abstract String msgTextKey();

}
