package bepicky.bot.client.message.handler.list.common;

import bepicky.bot.client.message.EntityType;
import bepicky.bot.client.message.button.CommandType;
import bepicky.bot.client.message.handler.list.AbstractCategoryListMessageHandler;
import bepicky.common.domain.response.CategoryListResponse;
import org.springframework.stereotype.Component;

import static bepicky.bot.client.message.template.TemplateUtils.LIST_PICKED_CATEGORIES;

@Component
public class PickedCommonCategoryListMessageHandler extends AbstractCategoryListMessageHandler {

    @Override
    protected CategoryListResponse getCategories(long chatId, int page) {
        return categoryService.listPicked(chatId, "COMMON", page, FOUR_PAGE_SIZE);
    }

    @Override
    protected String msgTextKey() {
        return LIST_PICKED_CATEGORIES;
    }

    @Override
    public String trigger() {
        return entityType().low();
    }

    @Override
    public CommandType commandType() {
        return CommandType.LIST_PICKED;
    }

    @Override
    public EntityType entityType() {
        return EntityType.CATEGORY;
    }
}
