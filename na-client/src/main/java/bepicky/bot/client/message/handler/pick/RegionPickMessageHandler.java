package bepicky.bot.client.message.handler.pick;

import bepicky.bot.core.message.EntityType;
import bepicky.bot.core.cmd.CommandType;
import bepicky.bot.client.message.handler.AbstractCategoryMessageHandler;
import bepicky.common.domain.response.CategoryResponse;
import org.springframework.stereotype.Component;

@Component
public class RegionPickMessageHandler extends AbstractCategoryMessageHandler {

    @Override
    protected CategoryResponse handle(Long chatId, Long categoryId) {
        return categoryService.pick(chatId, categoryId);
    }

    @Override
    public CommandType commandType() {
        return CommandType.PICK;
    }

    @Override
    public EntityType entityType() {
        return EntityType.REGION;
    }
}
