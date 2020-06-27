package vlad110kg.news.aggregator.bot.telegram.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vlad110kg.news.aggregator.bot.telegram.client.ReaderClient;
import vlad110kg.news.aggregator.bot.telegram.domain.Reader;
import vlad110kg.news.aggregator.bot.telegram.domain.request.ReaderRequest;

@Service
public class ReaderService implements IReaderService {

    @Autowired
    private ReaderClient readerClient;

    @Override
    public Reader register(ReaderRequest readerRequest) {
        return readerClient.register(readerRequest);
    }
}
