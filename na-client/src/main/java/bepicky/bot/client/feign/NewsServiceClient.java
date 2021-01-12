package bepicky.bot.client.feign;

import bepicky.common.domain.request.NewsSearchRequest;
import bepicky.common.domain.response.NewsSearchResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "bepicky-service", contextId = "news-client", configuration = FeignClientConfiguration.class)
public interface NewsServiceClient {

    @PostMapping("/news/search")
    NewsSearchResponse search(@RequestBody NewsSearchRequest request);
}
