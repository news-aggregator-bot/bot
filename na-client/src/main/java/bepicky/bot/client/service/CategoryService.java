package bepicky.bot.client.service;

import bepicky.bot.client.feign.NaServiceClient;
import bepicky.common.domain.dto.CategoryDto;
import bepicky.common.domain.request.PickCategoryRequest;
import bepicky.common.domain.response.ListCategoryResponse;
import bepicky.common.domain.response.PickCategoryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public CategoryDto find(long categoryId) {
        return null;
    }

    @Override
    public CategoryDto find(long chatID, long categoryId) {
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
