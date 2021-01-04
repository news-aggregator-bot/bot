package bepicky.bot.client.service;

import bepicky.bot.client.feign.ReaderServiceClient;
import bepicky.common.domain.dto.ReaderDto;
import bepicky.common.domain.dto.StatusReaderDto;
import bepicky.common.domain.request.ReaderRequest;
import bepicky.common.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ReaderService implements IReaderService {

    @Autowired
    private ReaderServiceClient readerClient;

    @Override
    public ReaderDto register(ReaderRequest readerRequest) {
        return readerClient.register(readerRequest);
    }

    @Override
    public ReaderDto find(Long chatId) {
        return checkReader(chatId, readerClient.find(chatId));
    }

    @Override
    public ReaderDto enable(Long chatId) {
        return checkReader(chatId, readerClient.enable(chatId));
    }

    @Override
    public ReaderDto disable(Long chatId) {
        return checkReader(chatId, readerClient.disable(chatId));
    }

    @Override
    public ReaderDto pause(Long chatId) {
        return checkReader(chatId, readerClient.pause(chatId));
    }

    @Override
    public ReaderDto settings(Long chatId) {
        return checkReader(chatId, readerClient.settings(chatId));
    }

    @Override
    public StatusReaderDto getStatus(Long chatId) {
        return readerClient.getStatus(chatId);
    }

    private ReaderDto checkReader(Long chatId, ReaderDto readerDto) {
        return Optional.ofNullable(readerDto)
            .orElseThrow(() -> new ResourceNotFoundException("404:reader:" + chatId));
    }
}
