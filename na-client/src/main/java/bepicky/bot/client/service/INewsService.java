package bepicky.bot.client.service;

import bepicky.common.domain.response.NewsSearchResponse;

public interface INewsService {

    NewsSearchResponse search(long chatId, String key, int page, int pageSize);
}
