package bepicky.bot.client.service;

import bepicky.common.domain.response.CategoryListResponse;
import bepicky.common.domain.response.CategoryResponse;

public interface ICategoryService {

    CategoryListResponse list(long chatId, String type, int page, int pageSize);

    CategoryListResponse list(long chatId, long parentId, int page, int pageSize);

    CategoryListResponse listPicked(long chatId, String type, int page, int pageSize);

    CategoryListResponse sublistPicked(long chatId, long parentId, int page, int pageSize);

    CategoryListResponse listNotPicked(long chatId, String type, int page, int pageSize);

    CategoryListResponse sublistNotPicked(long chatId, long parentId, int page, int pageSize);

    CategoryResponse pick(long chatId, long id);

    CategoryResponse remove(long chatId, long id);
}
