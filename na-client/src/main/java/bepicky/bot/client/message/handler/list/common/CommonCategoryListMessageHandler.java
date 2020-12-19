package bepicky.bot.client.message.handler.list.common;

import bepicky.bot.client.message.EntityType;
import bepicky.bot.client.message.command.CommandType;
import bepicky.bot.client.message.handler.list.AbstractCategoryListMessageHandler;
import bepicky.common.domain.response.CategoryListResponse;
import org.springframework.stereotype.Component;

import static bepicky.bot.client.message.template.TemplateUtils.LIST_CATEGORY;

@Component
public class CommonCategoryListMessageHandler extends AbstractCategoryListMessageHandler {

    @Override
    protected CategoryListResponse getCategories(long chatId, int page) {
        return categoryService.list(chatId, "COMMON", page, SIX_PAGE_SIZE);
    }

    @Override
    protected String msgTextKey() {
        return LIST_CATEGORY;
    }

    @Override
    public CommandType commandType() {
        return CommandType.LIST;
    }

    @Override
    public EntityType entityType() {
        return EntityType.CATEGORY;
    }
}
