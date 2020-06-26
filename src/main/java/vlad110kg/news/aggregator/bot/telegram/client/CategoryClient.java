package vlad110kg.news.aggregator.bot.telegram.client;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "na-service", path = "/category")
public interface CategoryClient {

}
