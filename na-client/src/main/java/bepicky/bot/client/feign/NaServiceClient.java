package bepicky.bot.client.feign;

import bepicky.bot.client.domain.Reader;
import bepicky.bot.client.domain.request.PickLanguageRequest;
import bepicky.bot.client.domain.request.ReaderRequest;
import bepicky.bot.client.domain.response.ListCategoryResponse;
import bepicky.bot.client.domain.response.PickLanguageResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import bepicky.bot.client.domain.request.PickCategoryRequest;
import bepicky.bot.client.domain.response.ListLanguageResponse;
import bepicky.bot.client.domain.response.PickCategoryResponse;

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
