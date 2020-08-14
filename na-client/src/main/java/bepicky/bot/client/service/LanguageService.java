package bepicky.bot.client.service;

import bepicky.bot.client.feign.NaServiceClient;
import bepicky.common.domain.request.LanguageRequest;
import bepicky.common.domain.response.LanguageListResponse;
import bepicky.common.domain.response.LanguageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LanguageService implements ILanguageService {

    @Autowired
    private NaServiceClient naServiceClient;

    @Override
    public LanguageListResponse list(long chatId, int page, int size) {
        return naServiceClient.listLanguages(chatId, page, size);
    }

    @Override
    public LanguageResponse pick(long chatId, String lang) {
        return naServiceClient.pick(req(chatId, lang));
    }

    @Override
    public LanguageResponse remove(long chatId, String lang) {
        return naServiceClient.remove(req(chatId, lang));
    }

    private LanguageRequest req(long chatId, String lang) {
        return new LanguageRequest(chatId, lang);
    }
}
