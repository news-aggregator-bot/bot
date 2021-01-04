package bepicky.bot.client.feign;

import bepicky.common.domain.request.LanguageRequest;
import bepicky.common.domain.response.LanguageListResponse;
import bepicky.common.domain.response.LanguageResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "bepicky-service", contextId = "language-client", configuration = FeignClientConfiguration.class)
public interface LanguageServiceClient {

    @PostMapping("/language/pick")
    LanguageResponse pick(@RequestBody LanguageRequest request);

    @PostMapping("/language/remove")
    LanguageResponse remove(@RequestBody LanguageRequest request);

    @GetMapping("/language/list")
    LanguageListResponse list(
        @RequestParam("chat_id") long chatId,
        @RequestParam("page") int page,
        @RequestParam("size") int size
    );
}
