package bepicky.bot.client.feign;

import bepicky.common.domain.request.SourceRequest;
import bepicky.common.domain.response.SourceListResponse;
import bepicky.common.domain.response.SourceResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "bepicky-service", contextId = "source-client", configuration = FeignClientConfiguration.class)
public interface SourceServiceClient {

    @GetMapping("/source/list")
    SourceListResponse list(
        @RequestParam("chat_id") long chatId,
        @RequestParam("page") int page,
        @RequestParam("size") int size
    );

    @PostMapping("/source/pick")
    SourceResponse pick(@RequestBody SourceRequest request);

    @PostMapping("/source/remove")
    SourceResponse remove(@RequestBody SourceRequest request);

}
