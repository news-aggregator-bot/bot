package vlad110kg.news.aggregator.bot.telegram.service;

import vlad110kg.news.aggregator.bot.telegram.domain.Category;
import vlad110kg.news.aggregator.bot.telegram.domain.ListCategoryResponse;

public interface ICategoryService {
    ListCategoryResponse list(long chatId, int page, int pageSize);

    ListCategoryResponse list(long chatId, long parentId, int page, int pageSize);

    Category find(long categoryId);

    Category find(long chatID, long categoryId);

    Category subscribe(long chatId, long id);
}
