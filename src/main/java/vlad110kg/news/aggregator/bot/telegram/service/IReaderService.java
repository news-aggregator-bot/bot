package vlad110kg.news.aggregator.bot.telegram.service;

import vlad110kg.news.aggregator.bot.telegram.domain.Reader;
import vlad110kg.news.aggregator.bot.telegram.domain.request.ReaderRequest;

public interface IReaderService {

    Reader register(ReaderRequest readerRequest);
}
