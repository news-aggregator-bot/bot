package bepicky.bot.client.service;

import bepicky.bot.client.feign.SourceServiceClient;
import bepicky.common.domain.request.SourceRequest;
import bepicky.common.domain.response.SourceListResponse;
import bepicky.common.domain.response.SourceResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SourceService implements ISourceService {

    @Autowired
    private SourceServiceClient sourceServiceClient;

    @Override
    public SourceListResponse list(long chatId, int page, int size) {
        return sourceServiceClient.list(chatId, page, size);
    }

    @Override
    public SourceResponse pick(long chatId, long id) {
        SourceRequest request = new SourceRequest();
        request.setChatId(chatId);
        request.setSourceId(id);
        return sourceServiceClient.pick(request);
    }

    @Override
    public SourceResponse remove(long chatId, long id) {
        SourceRequest request = new SourceRequest();
        request.setChatId(chatId);
        request.setSourceId(id);
        return sourceServiceClient.remove(request);
    }
}
