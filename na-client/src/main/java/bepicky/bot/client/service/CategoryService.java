package bepicky.bot.client.service;

import bepicky.bot.client.feign.NaServiceClient;
import bepicky.common.domain.request.CategoryRequest;
import bepicky.common.domain.response.CategoryListResponse;
import bepicky.common.domain.response.CategoryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryService implements ICategoryService {

    @Autowired
    private NaServiceClient categoryClient;

    @Override
    public CategoryListResponse list(long chatId, String type, int page, int pageSize) {
        return categoryClient.listCategories(chatId, page, pageSize, type);
    }

    @Override
    public CategoryListResponse list(long chatId, long parentId, int page, int pageSize) {
        return categoryClient.listSubcategories(chatId, parentId, page, pageSize);
    }

    @Override
    public CategoryListResponse listPicked(long chatId, String type, int page, int pageSize) {
        return categoryClient.listPickedCategories(chatId, page, pageSize, type);
    }

    @Override
    public CategoryListResponse sublistPicked(long chatId, long parentId, int page, int pageSize) {
        return categoryClient.sublistPickedCategories(chatId, parentId, page, pageSize);
    }

    @Override
    public CategoryListResponse listNotPicked(long chatId, String type, int page, int pageSize) {
        return categoryClient.listNotPickedCategories(chatId, page, pageSize, type);
    }

    @Override
    public CategoryListResponse sublistNotPicked(long chatId, long parentId, int page, int pageSize) {
        return categoryClient.sublistNotPickedCategories(chatId, parentId, page, pageSize);
    }

    @Override
    public CategoryResponse pick(long chatId, long id) {
        return categoryClient.pick(req(chatId, id));
    }

    @Override
    public CategoryResponse remove(long chatId, long id) {
        return categoryClient.remove(req(chatId, id));
    }

    private CategoryRequest req(long chatId, long id) {
        return new CategoryRequest(chatId, id);
    }
}
