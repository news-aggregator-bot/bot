package bepicky.bot.client.message.handler.list.region;

import bepicky.bot.client.message.EntityType;
import bepicky.bot.client.message.button.CommandType;
import bepicky.bot.client.message.handler.list.AbstractSubCategoryListMessageHandler;
import bepicky.bot.client.message.template.TemplateUtils;
import bepicky.common.domain.dto.CategoryDto;
import bepicky.common.domain.response.CategoryListResponse;
import org.springframework.stereotype.Component;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.util.Map;

import static bepicky.bot.client.message.template.TemplateUtils.LIST_PICKED_REGIONS;

@Component
public class PickedRegionSubCategoryListMessageHandler extends AbstractSubCategoryListMessageHandler {

    @Override
    public String trigger() {
        return entityType().low();
    }

    @Override
    protected CategoryListResponse getSubCategories(long chatId, long parentId, int page) {
        return categoryService.sublistPicked(chatId, parentId, page, PAGE_SIZE);
    }

    @Override
    protected Tuple2<String, Map<String, Object>> msgTextData(CategoryDto parent, int page) {
        return Tuples.of(LIST_PICKED_REGIONS, TemplateUtils.params("name", parent.getName(), "page", page));
    }

    @Override
    public CommandType commandType() {
        return CommandType.SUBLIST_PICKED;
    }

    @Override
    public EntityType entityType() {
        return EntityType.REGION;
    }
}
