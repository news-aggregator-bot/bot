package bepicky.bot.client.service;

import bepicky.bot.client.domain.Reader;
import bepicky.bot.client.domain.request.ReaderRequest;

public interface IReaderService {

    Reader register(ReaderRequest readerRequest);
}
