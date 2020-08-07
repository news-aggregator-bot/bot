package bepicky.bot.client.service;

import bepicky.common.domain.response.ListLanguageResponse;
import bepicky.common.domain.response.PickLanguageResponse;

public interface ILanguageService {

    ListLanguageResponse list(long chatId, int page, int size);

    PickLanguageResponse pick(long chatId, String lang);
}
