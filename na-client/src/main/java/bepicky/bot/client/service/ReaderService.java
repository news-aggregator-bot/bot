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
        return Optional.ofNullable(naServiceClient.find(chatId))
            .orElseThrow(() -> new ResourceNotFoundException("404:reader:" + chatId));
    }
}
