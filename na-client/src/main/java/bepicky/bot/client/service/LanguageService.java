package bepicky.bot.client.service;

import bepicky.bot.client.domain.response.PickLanguageResponse;
import bepicky.bot.client.feign.NaServiceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import bepicky.bot.client.domain.response.ListLanguageResponse;

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
