package bepicky.bot.client.message.handler.rm;

import bepicky.bot.core.message.EntityType;
import bepicky.bot.core.cmd.CommandType;
import bepicky.bot.client.message.handler.AbstractCategoryMessageHandler;
import bepicky.common.domain.response.CategoryResponse;
import org.springframework.stereotype.Component;

@Component
public class RegionRemoveMessageHandler extends AbstractCategoryMessageHandler {

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
        return EntityType.REGION;
    }
}
