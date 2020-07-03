package vlad110kg.news.aggregator.bot.telegram.service;

import vlad110kg.news.aggregator.bot.telegram.domain.response.ListLanguageResponse;
import vlad110kg.news.aggregator.bot.telegram.domain.response.PickLanguageResponse;

public interface ILanguageService {

    ListLanguageResponse list(long chatId, int page, int size);

    PickLanguageResponse pick(long chatId, String lang);
}
