package bepicky.bot.client.message.handler.rm;

import bepicky.bot.client.message.EntityType;
import bepicky.bot.client.message.handler.AbstractCategoryMessageHandler;
import bepicky.bot.client.message.template.TemplateUtils;
import bepicky.common.domain.response.CategoryResponse;
import org.springframework.stereotype.Component;

@Component
public class RegionRemoveAllMessageHandler extends AbstractCategoryMessageHandler implements RemoveAllMessageHandler {

    @Override
    protected String textKey() {
        return TemplateUtils.REMOVE_REGION_SUCCESS;
    }

    @Override
    protected CategoryResponse handle(Long chatId, Long categoryId) {
        return categoryService.removeAll(chatId, categoryId);
    }

    @Override
    public String trigger() {
        return EntityType.REGION.low();
    }
}
