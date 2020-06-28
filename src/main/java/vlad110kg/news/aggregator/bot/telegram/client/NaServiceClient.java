package vlad110kg.news.aggregator.bot.telegram.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import vlad110kg.news.aggregator.bot.telegram.domain.ListCategoryResponse;
import vlad110kg.news.aggregator.bot.telegram.domain.Reader;
import vlad110kg.news.aggregator.bot.telegram.domain.request.ReaderRequest;

@FeignClient(name = "na-service", configuration = FeignClientConfiguration.class)
public interface NaServiceClient {

    @GetMapping("/category/list")
    ListCategoryResponse list(
        @RequestParam("chat_id") long chatId,
        @RequestParam("page") int page,
        @RequestParam("size") int size
    );

    @GetMapping("/category/list")
    ListCategoryResponse list(
        @RequestParam("chat+id") long chatId,
        @RequestParam("parent_id") long parentId,
        @RequestParam("page") int page,
        @RequestParam("size") int size
    );

    @PostMapping("/reader/register")
    Reader register(ReaderRequest reader);
}
