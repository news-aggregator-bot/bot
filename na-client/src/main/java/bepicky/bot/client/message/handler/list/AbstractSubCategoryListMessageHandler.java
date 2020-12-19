package bepicky.bot.client.message.handler.list;

import bepicky.bot.client.message.button.InlineMarkupBuilder;
import bepicky.bot.client.message.command.ChatCommand;
import bepicky.bot.client.message.template.ButtonNames;
import bepicky.bot.client.message.template.TemplateUtils;
import bepicky.bot.client.service.ICategoryService;
import bepicky.common.domain.dto.CategoryDto;
import bepicky.common.domain.response.CategoryListResponse;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Override
    public HandleResult handle(ChatCommand cc) {
        long parentId = (long) cc.getId();
        int page = cc.getPage();

        CategoryListResponse response = getSubCategories(cc.getChatId(), parentId, page);
        if (response.isError()) {
            return error(response.getError().getEntity());
        }
        List<CategoryDto> categories = response.getList();
        CategoryDto parent = categories.get(0).getParent();
        boolean allCategoriesPicked = categories.stream().allMatch(CategoryDto::isPicked);

        InlineMarkupBuilder markup = new InlineMarkupBuilder();
        String readerLang = response.getReader().getLang();
        List<InlineMarkupBuilder.InlineButton> subcategoryButtons = categories.stream()
            .map(c -> new InlineMarkupBuilder.InlineButton(buildText(c, readerLang), buildCommand(c, parentId, page)))
            .collect(Collectors.toList());

        InlineMarkupBuilder.InlineButton allSubCategoriesBtn = buildAllSubCategoryButton(
            parentId,
            parent,
            allCategoriesPicked,
            markup,
            readerLang,
            page
        );
        InlineMarkupBuilder.InlineButton onlyParentBtn = buildOnlyParentCategoryButton(
            parentId,
            parent,
            markup,
            readerLang,
            page
        );
        markup.addButtons(Arrays.asList(allSubCategoriesBtn, onlyParentBtn));

        List<InlineMarkupBuilder.InlineButton> navigation = new ArrayList<>();
        if (!response.isFirst()) {
            String prevText = prevButtonText();
            navigation.add(markup.button(
                prevText,
                cmdMngr.sublist(entityType(), parentId, page - 1)
            ));
        }
        navigation.add(markup.button(backButtonText(readerLang), buildBackCommand(parent)));
        navigation.add(markup.button(
            doneButtonText(response.getReader().getLang()),
            cmdMngr.goNext(),
            chainManager.next(cc.getChatId()).getCommand()
        ));

        if (!response.isLast()) {
            String nextText = nextButtonText();
            navigation.add(markup.button(
                nextText,
                cmdMngr.sublist(entityType(), parentId, page + 1)
            ));
        }

        List<List<InlineMarkupBuilder.InlineButton>> partition = Lists.partition(subcategoryButtons, 2);
        partition.forEach(markup::addButtons);
        markup.addButtons(navigation);

        Tuple2<String, Map<String, Object>> textData = msgTextData(parent, page);
        String listSubcategoryText = parseToUnicode(templateContext.processTemplate(
            textData.getT1(),
            readerLang,
            textData.getT2()
        ));
        chainManager.updatePage(cc.getChatId(), page);
        return new HandleResult(listSubcategoryText, markup.build());
    }

    private InlineMarkupBuilder.InlineButton buildAllSubCategoryButton(
        long parentId,
        CategoryDto parent,
        boolean allChildrenPicked,
        InlineMarkupBuilder markup,
        String readerLang,
        int page
    ) {
        boolean picked = parent.isPicked() && allChildrenPicked;
        String allSubcategoriesText = parseToUnicode(templateContext.processTemplate(
            picked ? REMOVE_ALL_SUBCATEGORIES : PICK_ALL_SUBCATEGORIES,
            readerLang,
            TemplateUtils.name(parent.getLocalised())
        ));

        String parentCommand = picked ?
            cmdMngr.removeAll(entityType(), parentId) :
            cmdMngr.pickAll(entityType(), parentId);
        return markup.button(allSubcategoriesText, parentCommand, cmdMngr.sublist(entityType(), parentId, page));
    }

    private InlineMarkupBuilder.InlineButton buildOnlyParentCategoryButton(
        long parentId,
        CategoryDto parent,
        InlineMarkupBuilder markup,
        String readerLang,
        int page
    ) {
        String allSubcategoriesText = parseToUnicode(templateContext.processTemplate(
            parent.isPicked() ? ButtonNames.REMOVE : ButtonNames.PICK,
            readerLang,
            TemplateUtils.name(parent.getLocalised())
        ));

        String parentCommand = parent.isPicked() ?
            cmdMngr.remove(entityType(), parentId) :
            cmdMngr.pick(entityType(), parentId);
        return markup.button(allSubcategoriesText, parentCommand, cmdMngr.sublist(entityType(), parentId, page));
    }

    protected abstract CategoryListResponse getSubCategories(long chatId, long parentId, int page);

    protected abstract Tuple2<String, Map<String, Object>> msgTextData(CategoryDto parent, int page);

    private String buildBackCommand(CategoryDto parent) {
        return parent.getParent() == null ? cmdMngr.list(entityType())
            : cmdMngr.sublist(entityType(), parent.getParent().getId());
    }

    private List<String> buildCommand(CategoryDto c, long parent, int page) {
        if (c.getChildren() == null || c.getChildren().isEmpty()) {
            String cmd = c.isPicked() ?
                cmdMngr.remove(entityType(), c.getId()) :
                cmdMngr.pick(entityType(), c.getId());
            return Arrays.asList(cmd, cmdMngr.sublist(entityType(), parent, page));
        }
        return Arrays.asList(cmdMngr.sublist(entityType(), c.getId(), 1));
    }
}
