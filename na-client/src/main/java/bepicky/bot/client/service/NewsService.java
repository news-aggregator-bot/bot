package bepicky.bot.client.service;

import bepicky.bot.client.feign.NewsServiceClient;
import bepicky.common.domain.request.NewsSearchRequest;
import bepicky.common.domain.response.NewsSearchResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NewsService implements INewsService {

    @Autowired
    private NewsServiceClient newsServiceClient;

    @Override
    public NewsSearchResponse search(long chatId, String key, int page, int pageSize) {
        return newsServiceClient.search(new NewsSearchRequest(chatId, key, page, pageSize));
    }
}
