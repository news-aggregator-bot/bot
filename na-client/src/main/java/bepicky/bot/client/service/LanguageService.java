package bepicky.bot.client.service;

import bepicky.bot.client.feign.NaServiceClient;
import bepicky.common.domain.response.ListLanguageResponse;
import bepicky.common.domain.response.PickLanguageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
