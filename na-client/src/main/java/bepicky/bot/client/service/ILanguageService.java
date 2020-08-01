package bepicky.bot.client.service;

import bepicky.bot.client.domain.response.PickLanguageResponse;
import bepicky.bot.client.domain.response.ListLanguageResponse;

public interface ILanguageService {

    ListLanguageResponse list(long chatId, int page, int size);

    PickLanguageResponse pick(long chatId, String lang);
}
