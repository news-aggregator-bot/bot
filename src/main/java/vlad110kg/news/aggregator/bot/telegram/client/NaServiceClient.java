package vlad110kg.news.aggregator.bot.telegram.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import vlad110kg.news.aggregator.bot.telegram.domain.Reader;
import vlad110kg.news.aggregator.bot.telegram.domain.request.PickCategoryRequest;
import vlad110kg.news.aggregator.bot.telegram.domain.request.PickLanguageRequest;
import vlad110kg.news.aggregator.bot.telegram.domain.request.ReaderRequest;
import vlad110kg.news.aggregator.bot.telegram.domain.response.ListCategoryResponse;
import vlad110kg.news.aggregator.bot.telegram.domain.response.ListLanguageResponse;
import vlad110kg.news.aggregator.bot.telegram.domain.response.PickCategoryResponse;
import vlad110kg.news.aggregator.bot.telegram.domain.response.PickLanguageResponse;

@FeignClient(name = "na-service", configuration = FeignClientConfiguration.class)
public interface NaServiceClient {

    @GetMapping("/category/list")
    ListCategoryResponse listCategories(
        @RequestParam("chat_id") long chatId,
        @RequestParam("page") int page,
        @RequestParam("size") int size
    );

    @GetMapping("/category/list")
    ListCategoryResponse listSubcategories(
        @RequestParam("chat_id") long chatId,
        @RequestParam("parent_id") long parentId,
        @RequestParam("page") int page,
        @RequestParam("size") int size
    );

    @GetMapping("/language/list")
    ListLanguageResponse listLanguages(
        @RequestParam("chat_id") long chatId,
        @RequestParam("page") int page,
        @RequestParam("size") int size
    );

    @PostMapping("/category/pick")
    PickCategoryResponse pick(@RequestBody PickCategoryRequest request);

    @PostMapping("/language/pick")
    PickLanguageResponse pick(@RequestBody PickLanguageRequest request);

    @PostMapping("/reader/register")
    Reader register(ReaderRequest reader);
}
