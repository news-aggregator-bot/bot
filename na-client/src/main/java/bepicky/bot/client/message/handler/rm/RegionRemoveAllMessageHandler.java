package bepicky.bot.client.message.handler.rm;

import bepicky.bot.client.message.EntityType;
import bepicky.bot.client.message.command.CommandType;
import bepicky.bot.client.message.handler.AbstractCategoryMessageHandler;
import bepicky.common.domain.response.CategoryResponse;
import org.springframework.stereotype.Component;

@Component
public class RegionRemoveAllMessageHandler extends AbstractCategoryMessageHandler implements RemoveAllMessageHandler {

    @Override
    protected CategoryResponse handle(Long chatId, Long categoryId) {
        return categoryService.removeAll(chatId, categoryId);
    }

    @Override
    public CommandType commandType() {
        return CommandType.REMOVE_ALL;
    }

    @Override
    public EntityType entityType() {
        return EntityType.REGION;
    }
}
