package bepicky.bot.client.service;

import bepicky.bot.client.feign.NaServiceClient;
import bepicky.common.domain.dto.ReaderDto;
import bepicky.common.domain.request.ReaderRequest;
import bepicky.common.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ReaderService implements IReaderService {

    @Autowired
    private NaServiceClient naServiceClient;

    @Override
    public ReaderDto register(ReaderRequest readerRequest) {
        return naServiceClient.register(readerRequest);
    }

    @Override
    public ReaderDto find(Long chatId) {
        return checkReader(chatId, naServiceClient.find(chatId));
    }

    @Override
    public ReaderDto enable(Long chatId) {
        return checkReader(chatId, naServiceClient.enableReader(chatId));
    }

    @Override
    public ReaderDto disable(Long chatId) {
        return checkReader(chatId, naServiceClient.disableReader(chatId));
    }

    private ReaderDto checkReader(Long chatId, ReaderDto readerDto) {
        return Optional.ofNullable(readerDto)
            .orElseThrow(() -> new ResourceNotFoundException("404:reader:" + chatId));
    }
}
