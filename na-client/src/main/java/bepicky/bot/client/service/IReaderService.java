package bepicky.bot.client.service;

import bepicky.common.domain.dto.ReaderDto;
import bepicky.common.domain.dto.StatusReaderDto;
import bepicky.common.domain.request.ReaderRequest;

public interface IReaderService {

    ReaderDto register(ReaderRequest readerRequest);

    ReaderDto find(Long chatId);

    ReaderDto enable(Long chatId);

    ReaderDto disable(Long chatId);

    StatusReaderDto getStatus(Long chatId);
}
