package bepicky.bot.client.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import bepicky.bot.client.feign.NaServiceClient;
import bepicky.bot.client.domain.Category;
import bepicky.bot.client.domain.request.PickCategoryRequest;
import bepicky.bot.client.domain.response.ListCategoryResponse;
import bepicky.bot.client.domain.response.PickCategoryResponse;

@Service
public class CategoryService implements ICategoryService {

    @Autowired
    private NaServiceClient categoryClient;

    @Override
    public ListCategoryResponse list(long chatId, int page, int pageSize) {
        return categoryClient.listCategories(chatId, page, pageSize);
    }

    @Override
    public ListCategoryResponse list(long chatId, long parentId, int page, int pageSize) {
        return categoryClient.listSubcategories(chatId, parentId, page, pageSize);
    }

    @Override
    public Category find(long categoryId) {
        return null;
    }

    @Override
    public Category find(long chatID, long categoryId) {
        return null;
    }

    @Override
    public PickCategoryResponse pick(long chatId, long id) {
        return categoryClient.pick(PickCategoryRequest.builder()
            .chatId(chatId)
            .categoryId(id)
            .build());
    }
}