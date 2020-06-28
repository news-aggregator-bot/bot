package vlad110kg.news.aggregator.bot.telegram.service;

import vlad110kg.news.aggregator.bot.telegram.domain.Category;
import vlad110kg.news.aggregator.bot.telegram.domain.response.ListCategoryResponse;
import vlad110kg.news.aggregator.bot.telegram.domain.response.PickCategoryResponse;

public interface ICategoryService {
    ListCategoryResponse list(long chatId, int page, int pageSize);

    ListCategoryResponse list(long chatId, long parentId, int page, int pageSize);

    Category find(long categoryId);

    Category find(long chatID, long categoryId);

    PickCategoryResponse pick(long chatId, long id);
}
