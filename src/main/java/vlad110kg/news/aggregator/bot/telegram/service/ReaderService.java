package vlad110kg.news.aggregator.bot.telegram.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vlad110kg.news.aggregator.bot.telegram.client.NaServiceClient;
import vlad110kg.news.aggregator.bot.telegram.domain.Reader;
import vlad110kg.news.aggregator.bot.telegram.domain.request.ReaderRequest;

@Service
public class ReaderService implements IReaderService {

    @Autowired
    private NaServiceClient naServiceClient;

    @Override
    public Reader register(ReaderRequest readerRequest) {
        return naServiceClient.register(readerRequest);
    }
}
