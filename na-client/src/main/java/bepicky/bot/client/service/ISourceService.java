package bepicky.bot.client.service;

import bepicky.common.domain.response.SourceListResponse;
import bepicky.common.domain.response.SourceResponse;

public interface ISourceService {

    SourceListResponse list(long chatId, int page, int size);

    SourceResponse pick(long chatId, long id);

    SourceResponse remove(long chatId, long id);
}
