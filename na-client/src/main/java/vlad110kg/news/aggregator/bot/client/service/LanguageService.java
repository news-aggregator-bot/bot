package vlad110kg.news.aggregator.bot.client.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vlad110kg.news.aggregator.bot.client.feign.NaServiceClient;
import vlad110kg.news.aggregator.bot.client.domain.response.ListLanguageResponse;
import vlad110kg.news.aggregator.bot.client.domain.response.PickLanguageResponse;

@Service
public class LanguageService implements ILanguageService {

    @Autowired
    private NaServiceClient naServiceClient;

    @Override
    public ListLanguageResponse list(long chatId, int page, int size) {
        return naServiceClient.listLanguages(chatId, page, size);
    }

    @Override
    public PickLanguageResponse pick(
        long chatId, String lang
    ) {
        return null;
    }
}
