package bepicky.bot.client.message.handler.rm;

import bepicky.bot.client.message.EntityType;
import bepicky.bot.client.message.command.CommandType;
import bepicky.bot.client.message.handler.AbstractCategoryMessageHandler;
import bepicky.common.domain.response.CategoryResponse;
import org.springframework.stereotype.Component;

@Component
public class CategoryRemoveMessageHandler extends AbstractCategoryMessageHandler implements RemoveMessageHandler {

    @Override
    protected CategoryResponse handle(Long chatId, Long categoryId) {
        return categoryService.remove(chatId, categoryId);
    }

    @Override
    public CommandType commandType() {
        return CommandType.REMOVE;
    }

    @Override
    public EntityType entityType() {
        return EntityType.CATEGORY;
    }
}
