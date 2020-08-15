package bepicky.bot.client.service;

import bepicky.bot.client.feign.NaServiceClient;
import bepicky.common.domain.response.SourceListResponse;
import bepicky.common.domain.response.SourceResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SourceService implements ISourceService {

    @Autowired
    private NaServiceClient naServiceClient;

    @Override
    public SourceListResponse list(long chatId, int page, int size) {
        return null;
    }

    @Override
    public SourceResponse pick(long chatId, long id) {
        return null;
    }

    @Override
    public SourceResponse remove(long chatId, long id) {
        return null;
    }
}
