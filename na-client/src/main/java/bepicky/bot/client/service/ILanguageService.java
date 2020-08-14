package bepicky.bot.client.service;

import bepicky.common.domain.response.LanguageListResponse;
import bepicky.common.domain.response.LanguageResponse;

public interface ILanguageService {

    LanguageListResponse list(long chatId, int page, int size);

    LanguageResponse pick(long chatId, String lang);

    LanguageResponse remove(long chatId, String lang);

}
