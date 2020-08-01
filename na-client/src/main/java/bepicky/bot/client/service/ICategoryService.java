package bepicky.bot.client.service;

import bepicky.bot.client.domain.Category;
import bepicky.bot.client.domain.response.ListCategoryResponse;
import bepicky.bot.client.domain.response.PickCategoryResponse;

public interface ICategoryService {
    ListCategoryResponse list(long chatId, int page, int pageSize);

    ListCategoryResponse list(long chatId, long parentId, int page, int pageSize);

    Category find(long categoryId);

    Category find(long chatID, long categoryId);

    PickCategoryResponse pick(long chatId, long id);
}
