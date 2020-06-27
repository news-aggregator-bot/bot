package vlad110kg.news.aggregator.bot.telegram.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import vlad110kg.news.aggregator.bot.telegram.domain.ListCategoryResponse;

import javax.ws.rs.PathParam;

@FeignClient(name = "na-service", path = "/category", configuration = FeignClientConfiguration.class)
public interface CategoryClient {

    @GetMapping("/list")
    ListCategoryResponse list(
        @PathParam("chat_id") long chatId,
        @PathParam("page") int page,
        @PathParam("size") int size
    );

    @GetMapping("/list")
    ListCategoryResponse list(
        @PathParam("chat+id") long chatId,
        @PathParam("parent_id") long parentId,
        @PathParam("page") int page,
        @PathParam("size") int size
    );
}
