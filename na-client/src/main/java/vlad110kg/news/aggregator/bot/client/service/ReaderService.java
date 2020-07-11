package vlad110kg.news.aggregator.bot.client.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vlad110kg.news.aggregator.bot.client.feign.NaServiceClient;
import vlad110kg.news.aggregator.bot.client.domain.Reader;
import vlad110kg.news.aggregator.bot.client.domain.request.ReaderRequest;

@Service
public class ReaderService implements IReaderService {

    @Autowired
    private NaServiceClient naServiceClient;

    @Override
    public Reader register(ReaderRequest readerRequest) {
        return naServiceClient.register(readerRequest);
    }
}
