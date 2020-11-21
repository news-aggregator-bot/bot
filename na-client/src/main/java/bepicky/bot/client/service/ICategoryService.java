package bepicky.bot.client.service;

import bepicky.common.domain.response.CategoryListResponse;
import bepicky.common.domain.response.CategoryResponse;

public interface ICategoryService {

    CategoryListResponse list(long chatId, String type, int page, int pageSize);

    CategoryListResponse list(long chatId, long parentId, int page, int pageSize);

    CategoryResponse pick(long chatId, long id);

    CategoryResponse pickAll(long chatId, long id);

    CategoryResponse remove(long chatId, long id);

    CategoryResponse removeAll(long chatId, long id);
}
