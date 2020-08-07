package bepicky.bot.client.service;

import bepicky.common.domain.dto.CategoryDto;
import bepicky.common.domain.response.ListCategoryResponse;
import bepicky.common.domain.response.PickCategoryResponse;

public interface ICategoryService {
    ListCategoryResponse list(long chatId, int page, int pageSize);

    ListCategoryResponse list(long chatId, long parentId, int page, int pageSize);

    CategoryDto find(long categoryId);

    CategoryDto find(long chatID, long categoryId);

    PickCategoryResponse pick(long chatId, long id);
}
