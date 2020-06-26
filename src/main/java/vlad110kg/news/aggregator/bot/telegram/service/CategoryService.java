package vlad110kg.news.aggregator.bot.telegram.service;

import org.springframework.stereotype.Service;
import vlad110kg.news.aggregator.bot.telegram.domain.Category;
import vlad110kg.news.aggregator.bot.telegram.domain.ListCategoryResponse;

@Service
public class CategoryService implements ICategoryService {

    @Override
    public ListCategoryResponse list(int page, int pageSize) {
        return null;
    }

    @Override
    public ListCategoryResponse list(long parentId, int page, int pageSize) {
        return null;
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
