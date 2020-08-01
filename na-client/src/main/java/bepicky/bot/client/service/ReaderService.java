package bepicky.bot.client.service;

import bepicky.bot.client.domain.Reader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import bepicky.bot.client.feign.NaServiceClient;
import bepicky.bot.client.domain.request.ReaderRequest;

@Service
public class ReaderService implements IReaderService {

    @Autowired
    private NaServiceClient naServiceClient;

    @Override
    public Reader register(ReaderRequest readerRequest) {
        return naServiceClient.register(readerRequest);
    }
}
