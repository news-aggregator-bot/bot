package bepicky.bot.client.service;

import bepicky.bot.client.feign.CategoryServiceClient;
import bepicky.common.domain.request.CategoryRequest;
import bepicky.common.domain.response.CategoryListResponse;
import bepicky.common.domain.response.CategoryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryService implements ICategoryService {

    @Autowired
    private CategoryServiceClient categoryClient;

    @Override
    public CategoryListResponse list(long chatId, String type) {
        return categoryClient.list(chatId, type);
    }

    @Override
    public CategoryListResponse list(long chatId, String type, int page, int pageSize) {
        return categoryClient.list(chatId, page, pageSize, type);
    }

    @Override
    public CategoryListResponse list(long chatId, long parentId, int page, int pageSize) {
        return categoryClient.sublist(chatId, parentId, page, pageSize);
    }

    @Override
    public CategoryResponse pick(long chatId, long id) {
        return categoryClient.pick(req(chatId, id));
    }

    @Override
    public CategoryResponse pickAll(long chatId, long id) {
        return categoryClient.pickAll(req(chatId, id));
    }

    @Override
    public CategoryResponse remove(long chatId, long id) {
        return categoryClient.remove(req(chatId, id));
    }

    @Override
    public CategoryResponse removeAll(long chatId, long id) {
        return categoryClient.removeAll(req(chatId, id));
    }

    private CategoryRequest req(long chatId, long id) {
        return new CategoryRequest(chatId, id);
    }
}
