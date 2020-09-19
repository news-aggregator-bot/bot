package bepicky.bot.client.message.handler.rm;

import bepicky.bot.client.message.EntityType;
import bepicky.bot.client.message.handler.AbstractCategoryMessageHandler;
import bepicky.bot.client.message.template.TemplateUtils;
import bepicky.common.domain.response.CategoryResponse;
import org.springframework.stereotype.Component;

@Component
public class CategoryRemoveMessageHandler extends AbstractCategoryMessageHandler implements RemoveMessageHandler {

    @Override
    protected String textKey() {
        return TemplateUtils.REMOVE_CATEGORY_SUCCESS;
    }

    @Override
    protected CategoryResponse handle(Long chatId, Long categoryId) {
        return categoryService.remove(chatId, categoryId);
    }

    @Override
    public String trigger() {
        return EntityType.CATEGORY.lower();
    }
}
