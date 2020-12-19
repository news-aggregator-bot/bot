package bepicky.bot.client.message.handler.pick;

import bepicky.bot.client.message.EntityType;
import bepicky.bot.client.message.command.CommandType;
import bepicky.bot.client.message.handler.AbstractCategoryMessageHandler;
import bepicky.common.domain.response.CategoryResponse;
import org.springframework.stereotype.Component;

@Component
public class CategoryPickAllMessageHandler extends AbstractCategoryMessageHandler implements PickAllMessageHandler {

    @Override
    protected CategoryResponse handle(Long chatId, Long categoryId) {
        return categoryService.pickAll(chatId, categoryId);
    }

    @Override
    public CommandType commandType() {
        return CommandType.PICK_ALL;
    }

    @Override
    public EntityType entityType() {
        return EntityType.CATEGORY;
    }
}
