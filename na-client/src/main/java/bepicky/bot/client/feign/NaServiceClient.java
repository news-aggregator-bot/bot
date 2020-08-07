package bepicky.bot.client.feign;

import bepicky.common.domain.dto.ReaderDto;
import bepicky.common.domain.request.PickCategoryRequest;
import bepicky.common.domain.request.PickLanguageRequest;
import bepicky.common.domain.request.ReaderRequest;
import bepicky.common.domain.response.ListCategoryResponse;
import bepicky.common.domain.response.ListLanguageResponse;
import bepicky.common.domain.response.PickCategoryResponse;
import bepicky.common.domain.response.PickLanguageResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

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
    ReaderDto register(ReaderRequest reader);
}
