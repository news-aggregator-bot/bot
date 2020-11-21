package bepicky.bot.client.message.handler.pick;

import bepicky.bot.client.message.EntityType;
import bepicky.bot.client.message.handler.AbstractCategoryMessageHandler;
import bepicky.bot.client.message.template.TemplateUtils;
import bepicky.common.domain.response.CategoryResponse;
import org.springframework.stereotype.Component;

@Component
public class RegionPickAllMessageHandler extends AbstractCategoryMessageHandler implements PickAllMessageHandler {

    @Override
    protected String textKey() {
        return TemplateUtils.PICK_REGION_SUCCESS;
    }

    @Override
    protected CategoryResponse handle(Long chatId, Long categoryId) {
        return categoryService.pickAll(chatId, categoryId);
    }

    @Override
    public String trigger() {
        return EntityType.REGION.low();
    }
}
