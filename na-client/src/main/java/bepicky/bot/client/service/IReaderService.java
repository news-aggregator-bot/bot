package bepicky.bot.client.service;

import bepicky.common.domain.dto.ReaderDto;
import bepicky.common.domain.request.ReaderRequest;

public interface IReaderService {

    ReaderDto register(ReaderRequest readerRequest);
}
