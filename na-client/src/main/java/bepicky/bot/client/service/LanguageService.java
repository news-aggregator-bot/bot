package bepicky.bot.client.service;

import bepicky.bot.client.feign.LanguageServiceClient;
import bepicky.common.domain.request.LanguageRequest;
import bepicky.common.domain.response.LanguageListResponse;
import bepicky.common.domain.response.LanguageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LanguageService implements ILanguageService {

    @Autowired
    private LanguageServiceClient langClient;

    @Override
    public LanguageListResponse list(long chatId, int page, int size) {
        return langClient.list(chatId, page, size);
    }

    @Override
    public LanguageResponse pick(long chatId, String lang) {
        return langClient.pick(req(chatId, lang));
    }

    @Override
    public LanguageResponse remove(long chatId, String lang) {
        return langClient.remove(req(chatId, lang));
    }

    private LanguageRequest req(long chatId, String lang) {
        return new LanguageRequest(chatId, lang);
    }
}
