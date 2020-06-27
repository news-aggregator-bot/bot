package vlad110kg.news.aggregator.bot.telegram.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import vlad110kg.news.aggregator.bot.telegram.domain.Reader;
import vlad110kg.news.aggregator.bot.telegram.domain.request.ReaderRequest;

@FeignClient(name = "na-service", path = "/reader", configuration = FeignClientConfiguration.class)
public interface ReaderClient {

    @PostMapping
    Reader register(ReaderRequest reader);
}
