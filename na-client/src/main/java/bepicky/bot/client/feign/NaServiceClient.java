package bepicky.bot.client.feign;

import bepicky.common.domain.dto.ReaderDto;
import bepicky.common.domain.request.CategoryRequest;
import bepicky.common.domain.request.LanguageRequest;
import bepicky.common.domain.request.ReaderRequest;
import bepicky.common.domain.response.CategoryListResponse;
import bepicky.common.domain.response.CategoryResponse;
import bepicky.common.domain.response.LanguageListResponse;
import bepicky.common.domain.response.LanguageResponse;
import bepicky.common.domain.response.SourceListResponse;
import bepicky.common.domain.response.SourceResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "na-service", configuration = FeignClientConfiguration.class)
public interface NaServiceClient {

    @GetMapping("/category/list")
    CategoryListResponse listCategories(
        @RequestParam("chat_id") long chatId,
        @RequestParam("page") int page,
        @RequestParam("size") int size
    );

    @GetMapping("/category/list")
    CategoryListResponse listSubcategories(
        @RequestParam("chat_id") long chatId,
        @RequestParam("parent_id") long parentId,
        @RequestParam("page") int page,
        @RequestParam("size") int size
    );

    @GetMapping("/language/list")
    LanguageListResponse listLanguages(
        @RequestParam("chat_id") long chatId,
        @RequestParam("page") int page,
        @RequestParam("size") int size
    );

    @PostMapping("/category/pick")
    CategoryResponse pick(@RequestBody CategoryRequest request);

    @PostMapping("/category/remove")
    CategoryResponse remove(@RequestBody CategoryRequest request);

    @PostMapping("/language/pick")
    LanguageResponse pick(@RequestBody LanguageRequest request);

    @PostMapping("/language/remove")
    LanguageResponse remove(@RequestBody LanguageRequest request);

    @PostMapping("/reader/register")
    ReaderDto register(ReaderRequest reader);

    @GetMapping("/reader/{chatId}")
    ReaderDto find(@PathVariable("chatId") Long chatId);

    @GetMapping("/source/list")
    SourceListResponse listSources(
        @RequestParam("chat_id") long chatId,
        @RequestParam("page") int page,
        @RequestParam("size") int size
    );

    @PostMapping("/source/pick/{id}")
    SourceResponse pick(@PathVariable("id") long id);

    @PostMapping("/source/remove/{id}")
    SourceResponse remove(@PathVariable("id") long id);

    @PutMapping("/reader/enable/{chatId}")
    ReaderDto enableReader(@PathVariable("chatId") long id);

    @PutMapping("/reader/disable/{chatId}")
    ReaderDto disableReader(@PathVariable("chatId") long id);
}
