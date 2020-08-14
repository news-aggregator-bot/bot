package bepicky.bot.client.service;

import bepicky.common.domain.dto.CategoryDto;
import bepicky.common.domain.response.CategoryListResponse;
import bepicky.common.domain.response.CategoryResponse;

public interface ICategoryService {

    CategoryListResponse list(long chatId, int page, int pageSize);

    CategoryListResponse list(long chatId, long parentId, int page, int pageSize);

    CategoryDto find(long categoryId);

    CategoryDto find(long chatID, long categoryId);

    CategoryResponse pick(long chatId, long id);

    CategoryResponse remove(long chatId, long id);
}
