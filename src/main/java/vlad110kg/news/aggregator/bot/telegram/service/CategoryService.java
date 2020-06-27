package vlad110kg.news.aggregator.bot.telegram.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vlad110kg.news.aggregator.bot.telegram.client.CategoryClient;
import vlad110kg.news.aggregator.bot.telegram.domain.Category;
import vlad110kg.news.aggregator.bot.telegram.domain.ListCategoryResponse;

@Service
public class CategoryService implements ICategoryService {

    @Autowired
    private CategoryClient categoryClient;

    @Override
    public ListCategoryResponse list(long chatId, int page, int pageSize) {
        return categoryClient.list(chatId, page, pageSize);
    }

    @Override
    public ListCategoryResponse list(long chatId, long parentId, int page, int pageSize) {
        return categoryClient.list(chatId, parentId, page, pageSize);
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
    public Category subscribe(long chatId, long id) {
        return null;
    }
}
