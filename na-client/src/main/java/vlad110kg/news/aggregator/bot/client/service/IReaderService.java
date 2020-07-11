package vlad110kg.news.aggregator.bot.client.service;

import vlad110kg.news.aggregator.bot.client.domain.Reader;
import vlad110kg.news.aggregator.bot.client.domain.request.ReaderRequest;

public interface IReaderService {

    Reader register(ReaderRequest readerRequest);
}
